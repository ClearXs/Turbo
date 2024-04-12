package cc.allio.turbo.common.db.uno.repository.mybatis;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

/**
 * mybatis的{@link com.baomidou.mybatisplus.core.conditions.Wrapper}适配器，
 * <p>目的是能够通过该对象填充{@link cc.allio.uno.data.orm.dsl.Operator}的数据，以达到适配mybatis-plus ORM API的部分</p>
 *
 * @author j.x
 * @date 2024/2/5 12:19
 * @see #adapt(Wrapper, Object)
 * @since 0.1.0
 */
public class WrapperAdapter {

    private Class<?> entityClass;
    // 占位符值的存放，其key比如是#{ew.params.MPGENVAL1}
    private Map<String, Object> paramNameValuePairs;
    // 包含where group having等
    private final MergeSegments expression;
    // select 语句搜索的 select
    private final List<String> selects = Lists.newArrayList();
    // 确定当前wrapper的类型
    private final Effect effect;
    // 以{columnName, placeholder}组成的set pair，比如：a1=#{ew.paramNameValuePairs.MPGENVAL1}
    private final List<Tuple2<String, String>> setPair = Lists.newArrayList();

    // 支持的类型
    private static final List<Class<? extends Wrapper>> SUPPORTED_WRAPPER =
            Lists.newArrayList(QueryWrapper.class, LambdaQueryWrapper.class, UpdateWrapper.class, LambdaUpdateWrapper.class);

    private static Configuration configuration;

    WrapperAdapter(Wrapper<?> wrapper, Effect effect) {
        check(wrapper);
        this.effect = effect;
        this.expression = wrapper.getExpression();
        if (wrapper instanceof AbstractWrapper abstractWrapper) {
            this.paramNameValuePairs = abstractWrapper.getParamNameValuePairs();
            this.entityClass =
                    Optional.ofNullable(abstractWrapper.getEntityClass())
                            .or(() -> Optional.ofNullable(abstractWrapper.getEntity()).map(Object::getClass))
                            .orElse(null);
        }
        if (paramNameValuePairs == null) {
            this.paramNameValuePairs = Collections.emptyMap();
        }
        if (entityClass != null) {
            // build entity table info to mybatis plus (ignore has error)
            MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, entityClass.getName());
            TableInfoHelper.initTableInfo(builderAssistant, entityClass);
        }
        if (wrapper instanceof QueryWrapper<?> || wrapper instanceof LambdaQueryWrapper<?>) {
            if (effect == Effect.UPDATE) {
                throw new IllegalArgumentException("Operator type is 'Update' but wrapper is entityQuery, please make sure between type concurrency");
            }
            parseQueryWrapper(wrapper);
        } else if (wrapper instanceof UpdateWrapper<?> || wrapper instanceof LambdaUpdateWrapper<?>) {
            if (effect == Effect.QUERY) {
                throw new IllegalArgumentException("Operator type is 'Query' but wrapper is update, please make sure between type concurrency");
            }
            parseUpdateWrapper(wrapper);
        }
    }

    /**
     * 解析 Query Wrapper
     *
     * @param wrapper wrapper
     */
    private void parseQueryWrapper(Wrapper<?> wrapper) {
        String sqlSelect = StringPool.EMPTY;
        if (wrapper instanceof QueryWrapper<?> queryWrapper) {
            sqlSelect = queryWrapper.getSqlSelect();
        }
        if (wrapper instanceof LambdaQueryWrapper<?> lambdaQueryWrapper) {
            sqlSelect = lambdaQueryWrapper.getSqlSelect();
        }
        // default query all
        if (StringUtils.isBlank(sqlSelect)) {
            // find all entity field
            if (entityClass != null) {
                Collection<DSLName> columns = PojoWrapper.findColumns(entityClass);
                this.selects.addAll(columns.stream().map(DSLName::format).toList());
            }
            if (CollectionUtils.isEmpty(selects)) {
                this.selects.add(StringPool.ASTERISK);
            }
        } else {
            this.selects.addAll(Arrays.stream(sqlSelect.split(StringPool.COMMA)).toList());
        }
    }

    /**
     * 解析 Update Wrapper
     *
     * @param wrapper wrapper
     */
    private void parseUpdateWrapper(Wrapper<?> wrapper) {
        String sqlSet = StringPool.EMPTY;
        if (wrapper instanceof UpdateWrapper<?> updateWrapper) {
            sqlSet = updateWrapper.getSqlSet();
        }
        if (wrapper instanceof LambdaUpdateWrapper<?> lambdaUpdateWrapper) {
            sqlSet = lambdaUpdateWrapper.getSqlSet();
        }
        if (StringUtils.isNotBlank(sqlSet)) {
            List<Tuple2<String, String>> paris =
                    Arrays.stream(sqlSet.split(StringPool.COMMA))
                            .map(set -> {
                                String[] equalPairs = set.split(StringPool.EQUALS);
                                String left = equalPairs[0];
                                String right = equalPairs[1];
                                return Tuples.of(left, right);
                            })
                            .toList();
            setPair.addAll(paris);
        }
    }

    /**
     * wrapper data check
     */
    private <T> void check(Wrapper<T> wrapper) {
        if (wrapper == null) {
            throw new NullPointerException("Requirement wrapper not null");
        }
        Class<? extends Wrapper> wrapperClass = wrapper.getClass();
        var support = SUPPORTED_WRAPPER.stream().anyMatch(clazz -> clazz.isAssignableFrom(wrapperClass));
        if (!support) {
            throw new UnsupportedOperationException(String.format("Unsupported %s wrapper instance , " +
                    "nowadays support 'QueryWrapper' 'LambdaQueryWrapper' 'UpdateWrapper' 'LambdaUpdateWrapper'", wrapper.getClass().getName()));
        }
    }

    /**
     * 执行适配工作
     */
    public <T> T doAdapt(T operator) {
        WrapperAdaptor<T> adaptor = null;
        switch (effect) {
            case UPDATE -> adaptor = new UpdateWrapperAdaptor(setPair, paramNameValuePairs, expression, entityClass);
            case QUERY -> adaptor = new QueryWrapperAdaptor(selects, paramNameValuePairs, expression, entityClass);
            case WHERE -> adaptor = new WhereWrapperAdaptor(paramNameValuePairs, expression);
        }
        if (adaptor != null) {
            return adaptor.adapt(operator);
        }
        return operator;
    }

    /**
     * adapter创建方法。
     * <p>当前适配器只支持：</p>
     * <p>对于{@link QueryOperator}</p>
     * <ul>
     *     <li>{@link QueryOperator}</li>
     *     <li>{@link UpdateOperator}</li>
     *     <li>{@link WhereOperator}</li>
     * </ul>
     * <p>对于{@link Wrapper}</p>
     * <ul>
     *     <li>{@link QueryWrapper}</li>
     *     <li>{@link LambdaQueryWrapper}</li>
     *     <li>{@link UpdateWrapper}</li>
     *     <li>{@link LambdaUpdateWrapper}</li>
     * </ul>
     *
     * <b>remind: muse be exist entity class. like as use {@link Wrappers#lambdaQuery(Class)}、{@link Wrappers#lambdaUpdate(Class)} etc..., otherwise mybatis-plus will be not find any entity-class info</b>
     *
     * @param wrapper  mybatis-plus的{@link Wrapper}实例
     * @param operator uno-data orm的实例
     * @return operator 执行完适配返回
     * @throws UnsupportedOperationException 如果operator不支持或者wrapper不支持时抛出
     */
    public static <T> T adapt(Wrapper<?> wrapper, T operator) {
        Effect effect = null;
        if (operator instanceof QueryOperator) {
            effect = Effect.QUERY;
        } else if (operator instanceof UpdateOperator) {
            effect = Effect.UPDATE;
        } else if (operator instanceof WhereOperator<?>) {
            effect = Effect.WHERE;
        }
        if (effect == null) {
            throw new UnsupportedOperationException(String.format("Unsupported %s operator instance , " +
                    "nowadays support 'QueryOperator' 'UpdateOperator' 'WhereOperator'", operator.getClass().getName()));
        }
        return new WrapperAdapter(wrapper, effect).doAdapt(operator);
    }

    /**
     * init {@link WrapperAdapter}, make it use such as {@link LambdaQueryWrapper}, can be not error
     *
     * @param configuration configuration
     */
    public static void initiation(Configuration configuration) {
        WrapperAdapter.configuration = configuration;
    }
}