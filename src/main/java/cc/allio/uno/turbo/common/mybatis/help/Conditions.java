package cc.allio.uno.turbo.common.mybatis.help;

import cc.allio.uno.core.type.TypeOperator;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.turbo.common.constant.Direction;
import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import cc.allio.uno.turbo.common.web.params.EntityTerm;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.common.web.params.Order;
import cc.allio.uno.turbo.common.web.params.Term;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询条件示例构建器
 *
 * @author j.x
 * @date 2023/11/22 15:57
 * @since 0.1.0
 */
@Slf4j
public final class Conditions {

    private Conditions() {
    }

    static Map<Class<?>, TermCondition> specialConds = Maps.newHashMap();
    static TermCondition generalCond = new TermConditionImpl();

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
    public static <T extends IdEntity> QueryWrapper<T> query(@NonNull QueryParam<T> queryParam, @NonNull Class<T> entityType) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        // 查询条件
        List<Term> terms = queryParam.getTerms();
        if (CollectionUtils.isNotEmpty(terms)) {
            List<Field> fields = ReflectionKit.getFieldList(entityType);
            Map<String, Field> nameFields = fields.stream().collect(Collectors.toMap(Field::getName, f -> f));
            for (Term term : terms) {
                // 条件必须存在于实体字段之中
                if (!nameFields.containsKey(term.getField())) {
                    continue;
                }
                Field field = nameFields.get(term.getField());
                TermCondition termCondition = getCondByField(field);
                termCondition.doCondition(new EntityTerm(term, entityType, field), queryWrapper);
            }
        }

        // 排序
        List<Order> orders = queryParam.getOrders();
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                queryWrapper.orderBy(true, Direction.ASC == order.getDirection(), order.getProperty());
            }
        }
        return queryWrapper;
    }

    /**
     * 根据field获取condition实例
     *
     * @param field field实例
     */
    public static TermCondition getCondByField(Field field) {
        Class<?> type = field.getType();
        if (Date.class.isAssignableFrom(type)) {
            return specialConds.get(Date.class);
        } else if (String.class.isAssignableFrom(type)) {
            return specialConds.get(String.class);
        } else {
            return generalCond;
        }
    }

    public interface TermCondition {

        /**
         * 构建mp查询
         */
        <T extends IdEntity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper);

    }

    // 时间处理
    private static class DateTermCondition implements TermCondition {

        @Override
        public <T extends IdEntity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
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
                TypeOperator translator = TypeOperatorFactory.translator(Date.class);
                try {
                    Date firstTime = (Date) translator.convert(timeArray.get(0).toString());
                    Date endTime = (Date) translator.convert(timeArray.get(1).toString());
                    String tableColumn = MybatisKit.getTableColumn(term.getEntityField());
                    queryWrapper.le(tableColumn, firstTime);
                    queryWrapper.ge(tableColumn, endTime);
                } catch (Throwable ex) {
                    log.error("build term {} between time query has error", term, ex);
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
        public <T extends IdEntity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
            Object value = term.getValue();
            if (value != null) {
                String tableColumn = MybatisKit.getTableColumn(term.getEntityField());
                queryWrapper.like(tableColumn, value);
            }
        }
    }

    // 非特殊类型处理
    private static class TermConditionImpl implements TermCondition {

        @Override
        public <T extends IdEntity> void doCondition(EntityTerm term, QueryWrapper<T> queryWrapper) {
            Object value = term.getValue();
            if (value != null) {
                String tableColumn = MybatisKit.getTableColumn(term.getEntityField());
                TypeOperator operator = TypeOperatorFactory.translator(term.getEntityField().getType());
                if (operator != null) {
                    try {
                        queryWrapper.eq(tableColumn, operator.convert(value));
                    } catch (Throwable ex) {
                        log.error("build query term {} value {} has error", term, value, ex);
                    }
                } else {
                    queryWrapper.eq(tableColumn, value);
                }
            }
        }
    }
}
