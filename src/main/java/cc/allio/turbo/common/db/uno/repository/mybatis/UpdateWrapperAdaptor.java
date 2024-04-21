package cc.allio.turbo.common.db.uno.repository.mybatis;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;

/**
 * 适配{@link UpdateOperator}
 *
 * @author j.x
 * @date 2024/3/31 15:10
 * @since 0.1.1
 */
public class UpdateWrapperAdaptor<T extends UpdateOperator<?>> implements WrapperAdaptor<T> {

    private final List<Tuple2<String, String>> setPair;
    private final Map<String, Object> paramNameValuePairs;
    private final MergeSegments expression;
    private final Class<?> entityClass;

    public UpdateWrapperAdaptor(List<Tuple2<String, String>> setPair,
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
        new WhereWrapperAdaptor(paramNameValuePairs, expression).adapt(operator);
        for (Tuple2<String, String> pair : setPair) {
            String column = pair.getT1();
            String placeholder = pair.getT2();
            Object value = WrapperAdaptor.getValue(placeholder, paramNameValuePairs);
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
