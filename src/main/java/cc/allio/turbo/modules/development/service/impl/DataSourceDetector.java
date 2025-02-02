package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.db.entity.BaseEntity;
import cc.allio.turbo.common.domain.BehaviorSubscription;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.common.util.VariationAnalyzer;
import cc.allio.turbo.modules.development.constant.DataSourceStatus;
import cc.allio.turbo.modules.development.entity.DevDataSource;
import cc.allio.turbo.modules.development.service.IDevDataSourceService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.tx.TransactionContext;
import cc.allio.uno.data.tx.TxAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 数据源检测器。
 * <ul>
 *     <ul>{@link DevDataSource}数据变化监听</ul>
 *     <li>动态更新{@link CommandExecutorRegistry}注册表信息</li>
 *     <li>定时更新数据源状态</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/1/26 00:11
 * @since 0.1.0
 */
@Configuration
@EnableAutoConfiguration
@AutoConfigureAfter({MybatisPlusAutoConfiguration.class, TxAutoConfiguration.class})
public class DataSourceDetector implements DisposableBean, ApplicationListener<ApplicationStartedEvent> {

    private final CommandExecutorContext commandExecutorContext;
    private final IDevDataSourceService dataSourceService;
    private ScheduledExecutorService checkService;

    private final DataSourceComparator dataSourceComparator = new DataSourceComparator();
    // 内部维护数据源id与CommandExecutorKey的关系
    private final List<Disposable> discards = Lists.newArrayList();

    public DataSourceDetector(IDevDataSourceService dataSourceService, CommandExecutorContext commandExecutorContext) {
        this.dataSourceService = dataSourceService;
        this.commandExecutorContext = commandExecutorContext;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        discards.add(dataSourceService.subscribeOnInitialize().observe(this::onInitialization));
        // save
        discards.add(dataSourceService.subscribeOn(dataSourceService::save).observe(this::onSaveOrUpdate));
        // update
        discards.add(dataSourceService.subscribeOn("update").observe(this::onSaveOrUpdate));
        discards.add(dataSourceService.subscribeOn("saveOrUpdate").observe(this::onSaveOrUpdate));
        // remove
        discards.add(dataSourceService.subscribeOn(dataSourceService::remove).observe(this::onRemove));
        discards.add(dataSourceService.subscribeOn("removeById").observe(this::onRemove));
        discards.add(dataSourceService.subscribeOn("removeByIds").observe(this::onRemove));

        ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();
        // 注册表数据，状态检查服务
        this.checkService = new ScheduledThreadPoolExecutor(1, virtualThreadFactory);
        checkService.scheduleAtFixedRate(
                this::onDetectStatusChange,
                Duration.ofSeconds(1).toMillis(),
                Duration.ofMinutes(10).toMillis(),
                TimeUnit.MINUTES);
    }

    /**
     * 数据源初始化
     * <ul>
     *     <li>基于{@link CommandExecutorRegistry}的默认信息与数据库的默认数据源进行差异分析，做增加更新等操作</li>
     *     <li>把非默认数据源放入{@link CommandExecutorRegistry}中</li>
     * </ul>
     *
     * @param subscription subscription
     */
    void onInitialization(Subscription<DevDataSource> subscription) {
        TransactionContext.execute(() -> {
            List<AggregateCommandExecutor> allDefault = commandExecutorContext.getAllDefault();
            // 保存所有默认的
            List<DevDataSource> defaultDataSources =
                    allDefault
                            .stream()
                            .map(CommandExecutor::getOptions)
                            .map(dataSourceService::createByExecutorOptions)
                            .toList();
            List<DevDataSource> databaseDataSources = dataSourceService.list(Wrappers.<DevDataSource>lambdaQuery().eq(DevDataSource::isDefaulted, true));
            // 差异分析，确定默认数据源在数据库如何存储。如果数据库没有则新增，如果数据库的信息与默认信息有差异则更新，否则数据库信息删除
            VariationAnalyzer<DevDataSource, String> analyzer =
                    new VariationAnalyzer<>(defaultDataSources, databaseDataSources, DevDataSource::getKey, (a, b) -> dataSourceComparator.compare(a, b) != 0);
            VariationAnalyzer.AnalyzeResultSet<DevDataSource, String> resultSet = analyzer.analyze();

            List<VariationAnalyzer.Result<DevDataSource, String>> addition = resultSet.getAddition();
            List<DevDataSource> added =
                    addition.stream()
                            .map(VariationAnalyzer.Result::getSource)
                            .toList();
            dataSourceService.saveBatch(added);
            added.forEach(commandExecutorContext::lockWrite);

            List<VariationAnalyzer.Result<DevDataSource, String>> reduction = resultSet.getReduction();
            List<Long> removed = reduction.stream()
                    .map(VariationAnalyzer.Result::getBench)
                    .map(BaseEntity::getId)
                    .toList();
            dataSourceService.removeByIds(removed);

            List<VariationAnalyzer.Result<DevDataSource, String>> mutative = resultSet.getMutative();

            List<DevDataSource> changed = mutative.stream()
                    .map(result -> {
                        DevDataSource source = result.getSource();
                        DevDataSource bench = result.getBench();
                        source.setId(bench.getId());
                        return source;
                    })
                    .toList();
            for (DevDataSource change : changed) {
                dataSourceService.updateById(change);
                commandExecutorContext.lockWrite(change);
            }

            databaseDataSources.forEach(commandExecutorContext::lockWrite);
        });

        // 查询所有非默认的加入值 registry
        List<DevDataSource> nonDefault = dataSourceService.list(Wrappers.<DevDataSource>lambdaQuery().eq(DevDataSource::isDefaulted, false));
        nonDefault.stream()
                .peek(commandExecutorContext::lockWrite)
                .map(dataSourceService::createExecutorOptions)
                .forEach(commandExecutorContext::createAndRegister);
    }

    /**
     * 定时检测数据源以及状态变更
     */
    void onDetectStatusChange() {
        List<DevDataSource> all = dataSourceService.list();
        TransactionContext.execute(() -> {
            for (DevDataSource devDataSource : all) {
                commandExecutorContext.lockOptionGet(
                        devDataSource.getId(),
                        key -> {
                            CommandExecutor commandExecutor = commandExecutorContext.getCommandExecutor(key);
                            if (commandExecutor == null) {
                                ExecutorOptions executorOptions = dataSourceService.createExecutorOptions(devDataSource);
                                commandExecutor = commandExecutorContext.createAndRegister(executorOptions);
                            }
                            DataSourceStatus dataSourceStatus = internalCheck(commandExecutor);
                            devDataSource.setStatus(dataSourceStatus);
                            dataSourceService.updateById(devDataSource);
                        });
            }
        });
    }

    /**
     * 数据保存或者更新的回调。该回调做以下事情:
     * <ul>
     *     <li>如果更新则进行比较删除</li>
     *     <li>如果保存则新增CommandExecutor</li>
     *     <li>即使更新数据源的数据状态</li>
     * </ul>
     *
     * @param subscription 订阅信息
     */
    void onSaveOrUpdate(Subscription<DevDataSource> subscription) {
        Boolean saveOrUpdated = false;
        if (subscription instanceof BehaviorSubscription<DevDataSource> behaviorSubscription) {
            saveOrUpdated = behaviorSubscription.getBehaviorResult(Boolean.class).orElse(false);
        }
        DevDataSource dataSource = subscription.getDomain().orElse(null);
        if (Boolean.TRUE.equals(saveOrUpdated) && dataSource != null) {
            commandExecutorContext.lockOptionGet(
                    dataSource.getId(),
                    // 该回调是dataSource存在于registry中，做比较删除创建操作
                    key -> {
                        CommandExecutor commandExecutor = commandExecutorContext.getCommandExecutor(key);
                        DevDataSource executorDataSource = dataSourceService.createByExecutorOptions(commandExecutor.getOptions());
                        // 判断存在registry中的command与给定的数据源做比较。如果不相等则执行删除操作
                        if (dataSourceComparator.compare(dataSource, executorDataSource) != 0 &&
                                Boolean.TRUE.equals(commandExecutorContext.lockRemove(dataSource.getId(), commandExecutorContext::remove))) {
                            // 创建新的registry
                            ExecutorOptions executorOptions = dataSourceService.createExecutorOptions(dataSource);
                            commandExecutorContext.createAndRegister(executorOptions);
                        }
                    },
                    // 该回调是不存在于registry中，做创建操作
                    () -> {
                        ExecutorOptions executorOptions = dataSourceService.createExecutorOptions(dataSource);
                        if (executorOptions != null) {
                            commandExecutorContext.lockWrite(dataSource);
                            commandExecutorContext.createAndRegister(executorOptions);
                        }
                    },
                    // 该回调是事物完成后的补偿操作，此时基于DataSource的id一定可以找到某一个数据源，做DataSource状态更新操作
                    () -> {
                        String key = commandExecutorContext.lockGet(dataSource.getId());
                        CommandExecutor commandExecutor = commandExecutorContext.getCommandExecutor(key);
                        // 做检测更新数据源状态
                        DataSourceStatus dataSourceStatus = internalCheck(commandExecutor);
                        dataSourceService.update(
                                Wrappers.<DevDataSource>lambdaUpdate()
                                        .set(DevDataSource::getStatus, dataSourceStatus)
                                        .eq(DevDataSource::getId, dataSource.getId()));
                    });
        }
    }

    /**
     * 数据源删除进行移除
     */
    void onRemove(Subscription<DevDataSource> subscription) {
        if (subscription instanceof BehaviorSubscription<DevDataSource> behaviorSubscription) {
            Boolean removed = behaviorSubscription.getBehaviorResult(Boolean.class).orElse(false);
            behaviorSubscription.getParameter("list")
                    .ifPresent(removeIds -> {
                        if (removeIds instanceof Long id && Boolean.TRUE.equals(removed)) {
                            commandExecutorContext.lockRemove(id, commandExecutorContext::remove);
                        } else if (removeIds instanceof List<?> ids && Boolean.TRUE.equals(removed)) {
                            for (Object id : ids) {
                                commandExecutorContext.lockRemove((Long) id, commandExecutorContext::remove);
                            }
                        }
                    });
        }
    }

    @Override
    public void destroy() throws Exception {
        if (checkService != null) {
            checkService.close();
        }
        // 订阅通道关闭
        for (Disposable discard : discards) {
            if (!discard.isDisposed()) {
                discard.dispose();
            }
        }
    }

    private DataSourceStatus internalCheck(CommandExecutor commandExecutor) {
        try {
            boolean check = commandExecutor.check();
            if (check) {
                return DataSourceStatus.LIVED;
            } else {
                return DataSourceStatus.DEAD;
            }
        } catch (Throwable ex) {
            return DataSourceStatus.BLOCKED;
        }
    }

    static class DataSourceComparator implements Comparator<DevDataSource> {
        public int compare(DevDataSource a, DevDataSource b) {
            // 1.key
            // 2.连接账号
            // 3.连接密码
            // 4.连接地址
            // 5.engine
            return Comparator.comparing(DevDataSource::getKey)
                    .thenComparing(DevDataSource::getUsername)
                    .thenComparing(DevDataSource::getPassword)
                    .thenComparing(DevDataSource::getAddress)
                    .thenComparing(DevDataSource::getEngine)
                    .compare(a, b);
        }
    }

}
