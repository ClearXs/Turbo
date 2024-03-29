package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.uno.core.function.VoidConsumer;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorRegistry;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 用于维护{@link DevDataSource}与{@link AggregateCommandExecutor}之间的关系。
 * <p><b>该类里面的方法将进行加锁处理</b></p>
 * <ul>
 *     <li>提供{@link CommandExecutorRegistry}的所有方法</li>
 *     <li>提供用于维护{@link DevDataSource}与{@link AggregateCommandExecutor}key的api</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/1/29 13:02
 * @since 0.1.0
 */
@Slf4j
@Component
public class CommandExecutorContext implements CommandExecutorRegistry {

    private final CommandExecutorRegistry registry;

    // 内部维护数据源id与CommandExecutorKey的关系
    private final Map<Long, String> dataSourceIdKeyMap = Maps.newConcurrentMap();
    private final Lock lock = new ReentrantLock();

    public CommandExecutorContext(CommandExecutorRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T extends AggregateCommandExecutor> T crate(@NotNull ExecutorOptions executorOptions) {
        return registry.crate(executorOptions);
    }

    @Override
    public <T extends AggregateCommandExecutor> T createAndRegister(@NotNull ExecutorOptions executorOptions) {
        return registry.createAndRegister(executorOptions);
    }

    @Override
    public <T extends AggregateCommandExecutor> T registerCommandExecutor(ExecutorOptions executorOptions, Supplier<T> commandExecutorSupplier, boolean ifPresent) {
        return registry.registerCommandExecutor(executorOptions, commandExecutorSupplier, ifPresent);
    }

    @Override
    public <T extends AggregateCommandExecutor> T getCommandExecutor(ExecutorKey executorKey) {
        return registry.getCommandExecutor(executorKey);
    }

    @Override
    public <T extends AggregateCommandExecutor> T getCommandExecutor(String key) {
        return registry.getCommandExecutor(key);
    }

    @Override
    public boolean remove(ExecutorKey executorKey) {
        return registry.remove(executorKey);
    }

    @Override
    public boolean remove(String key) {
        return registry.remove(key);
    }

    @Override
    public boolean has(String key) {
        return registry.has(key);
    }

    @Override
    public boolean has(ExecutorKey executorKey) {
        return registry.has(executorKey);
    }

    @Override
    public List<AggregateCommandExecutor> getAllDefault() {
        return registry.getAllDefault();
    }

    @Override
    public List<AggregateCommandExecutor> getAll() {
        return registry.getAll();
    }

    @Override
    public void clear() {
        registry.clear();
    }

    /**
     * 使用加锁的方式根据指定的id获取维护用的CommandExecutor key，通过调用者提供的consumer进行回调
     *
     * @param id       id
     * @param consumer consumer
     */
    public void lockGet(Long id, Consumer<String> consumer) {
        lock.lock();
        try {
            String key = dataSourceIdKeyMap.get(id);
            if (consumer != null) {
                consumer.accept(key);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用加锁的方式根据指定的id获取维护用的CommandExecutor key
     *
     * @param id id
     */
    public String lockGet(Long id) {
        lock.lock();
        try {
            return dataSourceIdKeyMap.get(id);
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@link #lockOptionGet(Long, Consumer, VoidConsumer, VoidConsumer)}
     */
    public void lockOptionGet(Long id, Consumer<String> acceptor) {
        lockOptionGet(id, acceptor, null);
    }

    /**
     * {@link #lockOptionGet(Long, Consumer, VoidConsumer, VoidConsumer)}
     */
    public void lockOptionGet(Long id, Consumer<String> acceptor, VoidConsumer orElse) {
        lockOptionGet(id, acceptor, orElse, null);
    }

    /**
     * 与{@link #lockGet(Long, Consumer)}api类似，但如果key为空，则不进行回调。如果key为空，则调用者提供的orElse进行回调。在执行完成之后通过调用者提供的compensate进行回调
     *
     * @param id         {@link #dataSourceIdKeyMap}key
     * @param acceptor   如果通过id获取key存在的化进行回调
     * @param orElse     不存在则回调
     * @param compensate {@param acceptor}或者{@param orElse}执行完成之后调用
     */
    public void lockOptionGet(Long id, Consumer<String> acceptor, VoidConsumer orElse, VoidConsumer compensate) {
        lock.lock();
        try {
            String key = dataSourceIdKeyMap.get(id);
            if (StringUtils.isNotBlank(key) && acceptor != null) {
                tryExecuteAndCatchError(() -> acceptor.accept(key));
            } else if (orElse != null) {
                tryExecuteAndCatchError(orElse);
            }
        } finally {
            if (compensate != null) {
                tryExecuteAndCatchError(compensate);
            }
            lock.unlock();
        }
    }

    /**
     * 加锁写维护的关系
     */
    public void lockWrite(DevDataSource devDataSource) {
        lockWrite(devDataSource.getId(), devDataSource.getKey());
    }

    /**
     * 加锁写维护关系
     *
     * @param id  id
     * @param key key
     */
    public void lockWrite(Long id, String key) {
        lock.lock();
        try {
            dataSourceIdKeyMap.put(id, key);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 加锁移除，如果有回掉则把需要删除的Command Executor Key进行删除
     *
     * @param id       id
     * @param acceptor acceptor，该参数必须要求调用者返回true或者false
     */
    public Boolean lockRemove(Long id, Predicate<String> acceptor) {
        lock.lock();
        try {
            String removeKey = dataSourceIdKeyMap.remove(id);
            if (acceptor != null) {
                return StringUtils.isNotBlank(removeKey) && tryExecuteAndCatchError(() -> acceptor.test(removeKey));
            } else {
                return StringUtils.isNotBlank(removeKey);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 加锁判断指定的{@link DevDataSource#getId()}是否存在
     *
     * @param id id
     * @return if true exist, else not
     */
    public boolean lockContains(Long id) {
        lock.lock();
        try {
            return dataSourceIdKeyMap.containsKey(id);
        } finally {
            lock.unlock();
        }
    }

    private void tryExecuteAndCatchError(VoidConsumer execute) {
        try {
            execute.doAccept();
        } catch (Throwable ex) {
            log.error("execute {} has error, now try catch non throw any exception", execute, ex);
        }
    }

    private <T> T tryExecuteAndCatchError(Supplier<T> execute) {
        try {
            return execute.get();
        } catch (Throwable ex) {
            log.error("execute {} has error, now try catch non throw any exception", execute, ex);
            return null;
        }
    }
}
