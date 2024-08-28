package cc.allio.turbo.common.db.uno.repository.mybatis;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.function.lambda.MethodQueConsumer;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.segments.GroupBySegmentList;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.conditions.segments.OrderBySegmentList;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 关于where相关的操作适配器
 *
 * @author j.x
 * @date 2024/3/31 15:07
 * @since 0.1.1
 */
public class WhereWrapperAdaptor<T extends WhereOperator<T>> implements WrapperAdaptor<T> {

    private final Map<String, Object> paramNameValuePairs;
    private final MergeSegments expression;

    public WhereWrapperAdaptor(Map<String, Object> paramNameValuePairs, MergeSegments expression) {
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
                    // 进入下一次递归
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
                if (SqlKeyword.ASC == sqlKeyword) {
                    queryOperator.byAsc(column);
                } else if (SqlKeyword.DESC == sqlKeyword) {
                    queryOperator.byDesc(column);
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
                                        .map(pl -> WrapperAdaptor.getValue(pl, paramNameValuePairs))
                                        .toArray(Object[]::new);
                        multiValuesRef.set(values);
                        return token;
                    });
                    Object[] multiValues = multiValuesRef.get();
                    if (multiValues != null) {
                        value = multiValues;
                    }
                } else {
                    Object v = WrapperAdaptor.getValue(placeholder, paramNameValuePairs);
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

    record MultiSqlSegment(ISqlSegment... segments) implements ISqlSegment {

        MultiSqlSegment(ISqlSegment... segments) {
            this.segments =
                    Arrays.stream(segments)
                            .flatMap(segment -> {
                                if (segment instanceof MultiSqlSegment multiSqlSegment) {
                                    return Arrays.stream(multiSqlSegment.segments());
                                }
                                return Stream.of(segment);
                            })
                            .toArray(ISqlSegment[]::new);
        }

        @Override
        public String getSqlSegment() {
            // 包装成(#{xxx},#{xxx})
            return StringPool.LEFT_BRACKET +
                    Arrays.stream(segments).map(ISqlSegment::getSqlSegment).collect(Collectors.joining(StringPool.COMMA)) +
                    StringPool.RIGHT_BRACKET;
        }
    }
}
