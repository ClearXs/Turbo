package cc.allio.turbo.common.db.mybatis.helper;

import cc.allio.turbo.common.constant.Direction;
import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.type.TypeOperator;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.turbo.common.web.params.EntityTerm;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.common.web.params.Order;
import cc.allio.turbo.common.web.params.Term;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 查询条件示例构建器
 *
 * @author j.x
 * @date 2023/11/22 15:57
 * @since 0.1.0
 */
@Slf4j
public class Conditions {


    static Map<Class<?>, TermCondition> specialConds = Maps.newHashMap();
    static TermCondition generalCond = new TermConditionImpl();
    // 以实体为key，value存储{key=field name, value=field}
    static Map<Class<?>, Map<String, Field>> entityFields = Maps.newHashMap();

    static {
        specialConds.put(Date.class, new DateTermCondition());
        specialConds.put(String.class, new StringTermCondition());
    }

    /**
     * 通用参数构建{@link QueryWrapper}
     *
     * @param queryParam queryParam
     * @param <T>        某个实体类型
     * @return QueryWrapper instance
     */
    public static <T extends Entity> QueryWrapper<T> entityQuery(QueryParam<T> queryParam, Class<T> entityType) {
        return entityQuery(Wrappers.query(), queryParam, entityType);
    }

    /**
     * 通用参数构建{@link QueryWrapper}
     *
     * @param queryParam queryParam
     * @param <T>        某个实体类型
     * @return QueryWrapper instance
     */
    public static <T extends Entity> QueryWrapper<T> entityQuery(QueryWrapper<T> queryWrapper, QueryParam<T> queryParam, @NonNull Class<T> entityType) {
        Map<String, Field> nameFields =
                entityFields.computeIfAbsent(
                        entityType,
                        k -> {
                            List<Field> fields = ReflectionKit.getFieldList(entityType);
                            return fields.stream().collect(Collectors.toMap(Field::getName, f -> f));
                        });
        // 查询条件
        onQueryTerm(
                queryParam,
                nameFields::containsKey,
                term -> {
                    Field field = nameFields.get(term.getField());
                    EntityTerm entityTerm = new EntityTerm(term, entityType, field);
                    TermCondition termCondition = getCondByFieldType(entityTerm.getColumnType());
                    termCondition.doCondition(entityTerm, queryWrapper);
                });
        // order
        onOrderTerm(
                queryParam,
                nameFields::containsKey,
                term -> queryWrapper.orderBy(true, Direction.ASC == term.getDirection(), term.getProperty()));
        return queryWrapper;
    }

    /**
     * query term condition
     * <p>遍历{@link QueryParam#getTerms()}，选择满足参数contains满足的term，并回调</p>
     *
     * @param queryParam queryParam
     * @param contains   contains
     * @param acceptor   acceptor
     * @param <T>        实体类型
     */
    protected static <T extends Entity> void onQueryTerm(QueryParam<T> queryParam, Predicate<String> contains, Consumer<Term> acceptor) {
        List<Term> terms = queryParam.getTerms();
        if (CollectionUtils.isNotEmpty(terms)) {
            for (Term term : terms) {
                if (contains.test(term.getField())) {
                    acceptor.accept(term);
                }
            }
        }
    }

    /**
     * order term condition
     * <p>遍历{@link QueryParam#getOrders()}，选择满足参数contains满足的term，并回调</p>
     *
     * @param queryParam queryParam
     * @param contains   contains
     * @param acceptor   acceptor
     * @param <T>        实体类型
     */
    protected static <T extends Entity> void onOrderTerm(QueryParam<T> queryParam, Predicate<String> contains, Consumer<Order> acceptor) {
        List<Order> orders = queryParam.getOrders();
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                if (contains.test(order.getProperty())) {
                    acceptor.accept(order);
                }
            }
        }
    }

    /**
     * 根据fieldType获取condition实例
     *
     * @param fieldType fieldType
     */
    public static TermCondition getCondByFieldType(Class<?> fieldType) {
        if (Date.class.isAssignableFrom(fieldType)) {
            return specialConds.get(Date.class);
        } else if (String.class.isAssignableFrom(fieldType)) {
            return specialConds.get(String.class);
        } else {
            return generalCond;
        }
    }

    public interface TermCondition {

        /**
         * 构建mp查询
         */
        <T extends Entity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper);

    }

    // 时间处理
    private static class DateTermCondition implements TermCondition {

        @Override
        public <T extends Entity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
            // 形如 ["2023-01-01", "2023-01-01"]
            Object value = term.getValue();
            if (value != null &&
                    (value.getClass().isArray()
                            || List.class.isAssignableFrom(value.getClass())
                            || Set.class.isAssignableFrom(value.getClass()))
            ) {
                List<Object> timeArray = getTimeArray(value);
                if (CollectionUtils.isEmpty(timeArray)) {
                    return;
                }
                TypeOperator<Date> translator = TypeOperatorFactory.translator(Date.class);
                try {
                    Date firstTime = translator.convert(timeArray.get(0).toString());
                    Date endTime = translator.convert(timeArray.get(1).toString());
                    String tableColumn = term.getTableColumn();
                    queryWrapper.le(tableColumn, firstTime);
                    queryWrapper.ge(tableColumn, endTime);
                } catch (Throwable ex) {
                    log.error("build term {} between time entityQuery has error", term, ex);
                }
            }
        }

        private List<Object> getTimeArray(Object value) {
            Class<?> valueClass = value.getClass();
            if (List.class.isAssignableFrom(valueClass)) {
                return (List) value;
            } else if (Set.class.isAssignableFrom(valueClass)) {
                return Lists.newArrayList((Set) value);
            } else if (valueClass.isArray()) {
                return Lists.newArrayList(value);
            }
            return Collections.emptyList();
        }
    }

    // 字符串处理
    private static class StringTermCondition implements TermCondition {

        @Override
        public <T extends Entity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
            Object value = term.getValue();
            if (value != null) {
                String tableColumn = term.getTableColumn();
                queryWrapper.like(tableColumn, value);
            }
        }
    }

    // 非特殊类型处理
    private static class TermConditionImpl implements TermCondition {

        @Override
        public <T extends Entity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
            Object value = term.getValue();
            if (value != null) {
                String tableColumn = term.getTableColumn();
                TypeOperator<?> operator = TypeOperatorFactory.translator(term.getColumnType());
                if (operator != null) {
                    try {
                        queryWrapper.eq(tableColumn, operator.convert(value));
                    } catch (Throwable ex) {
                        log.error("build entityQuery term {} value {} has error", term, value, ex);
                    }
                } else {
                    queryWrapper.eq(tableColumn, value);
                }
            }
        }
    }
}
