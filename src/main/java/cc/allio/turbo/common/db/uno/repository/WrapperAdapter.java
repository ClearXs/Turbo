package cc.allio.turbo.common.db.uno.repository;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.function.lambda.MethodQueConsumer;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.TokenParser;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.GroupBySegmentList;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.conditions.segments.OrderBySegmentList;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.google.common.collect.Lists;
import lombok.Getter;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final Class<?> entityClass;
    // 占位符值的存放，其key比如是#{ew.params.MPGENVAL1}
    private final Map<String, Object> paramNameValuePairs;
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

    private static final TokenParser HASH_BRACE_TOKEN_PARSER = ExpressionTemplate.createParse(Tokenizer.HASH_BRACE);
    private static final TokenParser BRACE_TOKEN_PARSER = ExpressionTemplate.createParse(Tokenizer.DOUBLE_BRACKET);

    WrapperAdapter(Wrapper<?> wrapper, Effect effect) {
        check(wrapper);
        this.effect = effect;
        this.expression = wrapper.getExpression();
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
        if (wrapper instanceof AbstractWrapper<?, ?, ?> abstractWrapper) {
            this.paramNameValuePairs = abstractWrapper.getParamNameValuePairs();
            this.entityClass = abstractWrapper.getEntityClass();
        } else {
            this.paramNameValuePairs = Collections.emptyMap();
            this.entityClass = null;
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
        if (StringUtils.isNotBlank(sqlSelect)) {
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
        InternalAdapter<T> adapter = null;
        switch (effect) {
            case UPDATE -> adapter = new UpdateWrapperAdapter(setPair, paramNameValuePairs, expression, entityClass);
            case QUERY -> adapter = new QueryWrapperAdapter(selects, paramNameValuePairs, expression, entityClass);
            case WHERE -> adapter = new WhereWrapperAdapter(paramNameValuePairs, expression);
        }
        if (adapter != null) {
            return adapter.adapt(operator);
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
     * 适配{@link UpdateOperator}
     */
    static class UpdateWrapperAdapter<T extends UpdateOperator> implements InternalAdapter<T> {

        private final List<Tuple2<String, String>> setPair;
        private final Map<String, Object> paramNameValuePairs;
        private final MergeSegments expression;
        private final Class<?> entityClass;

        public UpdateWrapperAdapter(List<Tuple2<String, String>> setPair,
                                    Map<String, Object> paramNameValuePairs,
                                    MergeSegments expression,
                                    Class<?> entityClass) {
            this.setPair = setPair;
            this.paramNameValuePairs = paramNameValuePairs;
            this.expression = expression;
            this.entityClass = entityClass;
        }

        @Override
        public T adapt(T operator) {
            new WhereWrapperAdapter<UpdateOperator>(paramNameValuePairs, expression).adapt(operator);
            for (Tuple2<String, String> pair : setPair) {
                String column = pair.getT1();
                String placeholder = pair.getT2();
                Object value = getValue(placeholder, paramNameValuePairs);
                if (value == null) {
                    value = ValueWrapper.EMPTY_VALUE;
                }
                operator.update(column, value);
            }
            // from
            if (entityClass != null) {
                operator.from(entityClass);
            }
            return operator;
        }
    }

    /**
     * 适配{@link QueryOperator}
     */
    static class QueryWrapperAdapter<T extends QueryOperator> implements InternalAdapter<T> {

        private final Map<String, Object> paramNameValuePairs;
        private final MergeSegments expression;
        private final List<String> selects;
        private final Class<?> entityClass;

        public QueryWrapperAdapter(List<String> selects,
                                   Map<String, Object> paramNameValuePairs,
                                   MergeSegments expression,
                                   Class<?> entityClass) {
            this.paramNameValuePairs = paramNameValuePairs;
            this.expression = expression;
            this.selects = selects;
            this.entityClass = entityClass;
        }

        @Override
        public T adapt(T operator) {
            new WhereWrapperAdapter<QueryOperator>(paramNameValuePairs, expression).adapt(operator);
            for (String select : selects) {
                operator.select(select);
            }
            // from
            if (entityClass != null) {
                operator.from(entityClass);
            }
            return operator;
        }
    }

    /**
     * 关于where相关的操作适配器
     */
    static class WhereWrapperAdapter<T extends WhereOperator<T>> implements InternalAdapter<T> {

        private final Map<String, Object> paramNameValuePairs;
        private final MergeSegments expression;

        public WhereWrapperAdapter(Map<String, Object> paramNameValuePairs, MergeSegments expression) {
            this.paramNameValuePairs = paramNameValuePairs;
            this.expression = expression;
        }

        @Override
        public T adapt(T operator) {
            // where
            NormalSegmentList normal = expression.getNormal();
            whereAdapt(operator, normal);
            // order
            OrderBySegmentList order = expression.getOrderBy();
            orderAdapt(operator, order);
            // group
            GroupBySegmentList group = expression.getGroupBy();
            groupAdapt(operator, group);
            return operator;
        }

        /**
         * where 条件适配
         *
         * @param operator operator
         * @param normal   where condition
         */
        private void whereAdapt(T operator, NormalSegmentList normal) {
            // where operator 条件注入
            WhereInjector<T> injector = new WhereInjector<>(operator, paramNameValuePairs);
            // 条件连接的谓词
            SqlKeyword logic = null;
            ISqlSegment left = null;
            SqlKeyword predicate = null;
            ISqlSegment right = null;

            SqlKeyword prePredicate = null;
            // 注入between之后该项被赋值为true，目的还原三元+一逻的规律
            boolean injectBetween = false;

            // mybatis-plus的where逻辑处理是，以left、predicate、right三个连续的ISqlSegment存放在NormalSegmentList中（下简称为三元+一逻）
            // 随后跟上一个逻辑连词进行拼接，之后在按照三元+一逻往复
            // 存在特殊情况：
            // 1.mybatis-plus为了简单拼接SQL，把between的条件连续5个放在一起，并且语句结束之后下一个一定是ISqlSegment是逻辑谓词（将打乱where基本处理的顺序）
            // 2.is null、is not null将导致无right条件
            // 3.like、not like没有LEFT LIKE、RIGHT LIKE等等predicate
            for (ISqlSegment sqlSegment : normal) {
                if (sqlSegment instanceof SqlKeyword sqlKeyword) {
                    // 之后加的BETWEEN的判断是为了还原三元+一逻的规律
                    if (SqlKeyword.AND == sqlKeyword || SqlKeyword.OR == sqlKeyword) {
                        if ((SqlKeyword.BETWEEN == prePredicate || SqlKeyword.NOT_BETWEEN == prePredicate)
                                && injectBetween) {
                            logic = null;
                        } else {
                            logic = sqlKeyword;
                        }
                    } else {
                        predicate = sqlKeyword;
                        prePredicate = predicate;
                    }
                } else {
                    // 优先拼接left
                    if (left == null) {
                        left = sqlSegment;
                    } else {
                        if (right != null) {
                            right = new MultiSqlSegment(right, sqlSegment);
                        } else {
                            right = sqlSegment;
                        }
                    }
                }
                if (logic != null) {
                    // 这里条件复杂，但目的是为了避免通用的三元+一逻轮回的方法失效，做的特殊处理
                    // 即使得right的实例是MultiSqlSegment
                    if ((SqlKeyword.BETWEEN == predicate || SqlKeyword.NOT_BETWEEN == predicate) &&
                            (right != null && !MultiSqlSegment.class.isAssignableFrom(right.getClass()))) {
                        // 使right成为MultiSqlSegment实例
                        continue;
                    }
                    // 避免初始时logic就有值
                    if (left != null && predicate != null) {
                        injector.accept(left, logic, predicate, right);
                        injectBetween = SqlKeyword.BETWEEN == predicate || SqlKeyword.NOT_BETWEEN == predicate;
                        // 进入下一次轮回
                        left = null;
                        predicate = null;
                        right = null;
                        logic = null;
                    }
                }
            }
            // 当退出循环之后，可能存在单条件的ISqlSegment，需要进行处理
            if (left != null && predicate != null) {
                injector.accept(left, logic, predicate, right);
            }
        }

        /**
         * order by 条件适配
         *
         * @param operator  operator
         * @param orderList order by condition
         */
        private void orderAdapt(T operator, OrderBySegmentList orderList) {
            if (operator instanceof QueryOperator queryOperator) {
                for (ISqlSegment sqlSegment : orderList) {
                    // 如 name ASC
                    String orderColumnKeyWord = sqlSegment.getSqlSegment();
                    String[] columnKeyWords = orderColumnKeyWord.split(StringPool.SPACE);
                    String column = columnKeyWords[0];
                    String keyword = columnKeyWords[1];
                    SqlKeyword sqlKeyword = SqlKeyword.valueOf(keyword);
                    if (sqlKeyword != null) {
                        if (SqlKeyword.ASC == sqlKeyword) {
                            queryOperator.byAsc(column);
                        } else if (SqlKeyword.DESC == sqlKeyword) {
                            queryOperator.byDesc(column);
                        }
                    }
                }
            }
        }

        /**
         * group by 条件适配
         *
         * @param operator  operator
         * @param groupList group by condition
         */
        private void groupAdapt(T operator, GroupBySegmentList groupList) {
            if (operator instanceof QueryOperator queryOperator) {
                String[] columns = groupList.stream().map(ISqlSegment::getSqlSegment).toArray(String[]::new);
                queryOperator.groupByOnes(columns);
            }
        }
    }

    public interface InternalAdapter<T> {

        /**
         * 基于uno-data的{@link cc.allio.uno.data.orm.dsl.Operator}实例进行适配
         *
         * @param operator operator
         * @return operator
         */
        T adapt(T operator);
    }

    /**
     * 适配器的作用效果
     */
    enum Effect {
        QUERY, UPDATE, WHERE
    }

    static class WhereInjector<T extends WhereOperator<T>> implements MethodQueConsumer<ISqlSegment, SqlKeyword, SqlKeyword, ISqlSegment> {

        private final T whereOperator;
        private final Map<String, Object> paramNameValuePairs;

        public WhereInjector(T whereOperator, Map<String, Object> paramNameValuePairs) {
            this.whereOperator = whereOperator;
            this.paramNameValuePairs = paramNameValuePairs;
        }

        @Override
        public void accept(ISqlSegment left, SqlKeyword logic, SqlKeyword predicate, ISqlSegment right) {
            String column = left.getSqlSegment();
            Object value = null;
            if (right != null) {
                String placeholder = right.getSqlSegment();
                // 多值赋值
                if (SqlKeyword.IN == predicate ||
                        SqlKeyword.NOT_IN == predicate ||
                        SqlKeyword.BETWEEN == predicate ||
                        SqlKeyword.NOT_BETWEEN == predicate) {
                    AtomicReference<Object[]> multiValuesRef = new AtomicReference<>(null);
                    BRACE_TOKEN_PARSER.parse(placeholder, token -> {
                        String[] multiPlaceholders = token.split(StringPool.COMMA);
                        Object[] values =
                                Arrays.stream(multiPlaceholders)
                                        .map(pl -> getValue(pl, paramNameValuePairs))
                                        .toArray(Object[]::new);
                        multiValuesRef.set(values);
                        return token;
                    });
                    Object[] multiValues = multiValuesRef.get();
                    if (multiValues != null) {
                        value = multiValues;
                    }
                } else {
                    Object v = getValue(placeholder, paramNameValuePairs);
                    if (v != null) {
                        value = v;
                    }
                }
            }
            if (value == null) {
                value = ValueWrapper.EMPTY_VALUE;
            }
            inject(whereOperator, logic, predicate, column, value);
        }

        /**
         * 基于 {@link SqlKeyword}的谓词信息，使用{@link WhereOperator}进行对应的条件注入
         *
         * @param whereOperator whereOperator
         * @param logic         logic
         * @param sqlKeyword    sqlKeyword
         * @param column        column
         * @param v             v
         */
        private void inject(T whereOperator, SqlKeyword logic, SqlKeyword sqlKeyword, String column, Object v) {
            if (SqlKeyword.AND == logic) {
                whereOperator.and();
            } else if (SqlKeyword.OR == logic) {
                whereOperator.or();
            }
            switch (sqlKeyword) {
                case EQ -> whereOperator.eq(column, v);
                case GE -> whereOperator.gte(column, v);
                case GT -> whereOperator.gt(column, v);
                case LE -> whereOperator.lte(column, v);
                case LT -> whereOperator.lt(column, v);
                case NE -> whereOperator.neq(column, v);
                case LIKE -> whereOperator.like(column, v);
                case NOT_LIKE -> whereOperator.notLike(column, v);
                case IS_NULL -> whereOperator.isNull(column);
                case IN -> whereOperator.in(column, v);
                case NOT_IN -> whereOperator.notIn(column, v);
                case BETWEEN -> {
                    if (v.getClass().isArray()) {
                        Object[] values = (Object[]) v;
                        if (values.length >= 2) {
                            whereOperator.between(column, values[0], values[1]);
                        }
                    }
                }
                case NOT_BETWEEN -> {
                    if (v.getClass().isArray()) {
                        Object[] values = (Object[]) v;
                        if (values.length >= 2) {
                            whereOperator.notBetween(column, values[0], values[1]);
                        }
                    }
                }
                case IS_NOT_NULL -> whereOperator.notNull(column);
                default -> {
                }
            }
        }
    }

    /**
     * 基于{@link TokenParser}在paramNameValuePairs的Map中获取占位符的值
     *
     * @param placeholder         placeholder
     * @param paramNameValuePairs paramNameValuePairs
     * @return value or null
     */
    static Object getValue(String placeholder, Map<String, Object> paramNameValuePairs) {
        if (StringUtils.isBlank(placeholder)) {
            return null;
        }
        AtomicReference<Object> valueRef = new AtomicReference<>(null);
        HASH_BRACE_TOKEN_PARSER.parse(placeholder, token -> {
            String symbolise = token.substring(token.lastIndexOf(StringPool.ORIGIN_DOT) + 1);
            Object v = paramNameValuePairs.get(symbolise);
            if (v != null) {
                valueRef.set(v);
            }
            return token;
        });
        return valueRef.get();
    }

    static class MultiSqlSegment implements ISqlSegment {

        @Getter
        private final ISqlSegment[] segments;

        public MultiSqlSegment(ISqlSegment... segments) {
            this.segments =
                    Arrays.stream(segments)
                            .flatMap(segment -> {
                                if (segment instanceof MultiSqlSegment multiSqlSegment) {
                                    return Arrays.stream(multiSqlSegment.getSegments());
                                }
                                return Stream.of(segment);
                            })
                            .toArray(ISqlSegment[]::new);
        }

        @Override
        public String getSqlSegment() {
            // 包装成(#{xxx},#{xxx})
            return StringPool.LEFT_BRACKET + Arrays.stream(segments).map(ISqlSegment::getSqlSegment).collect(Collectors.joining(StringPool.COMMA)) + StringPool.RIGHT_BRACKET;
        }
    }
}
