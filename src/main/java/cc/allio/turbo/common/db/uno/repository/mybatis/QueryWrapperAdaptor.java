package cc.allio.turbo.common.db.uno.repository.mybatis;

import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;

import java.util.List;
import java.util.Map;

/**
 * 适配QueryOperator
 *
 * @author j.x
 * @date 2024/3/31 15:09
 * @since 0.1.1
 */
public class QueryWrapperAdaptor<T extends QueryOperator> implements WrapperAdaptor<T> {

    private final Map<String, Object> paramNameValuePairs;
    private final MergeSegments expression;
    private final List<String> selects;
    private final Class<?> entityClass;

    public QueryWrapperAdaptor(List<String> selects,
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
        for (String select : selects) {
            operator.select(select);
        }
        new WhereWrapperAdaptor<QueryOperator>(paramNameValuePairs, expression).adapt(operator);
        // from
        if (entityClass != null) {
            operator.from(entityClass);
        }
        return operator;
    }
}
