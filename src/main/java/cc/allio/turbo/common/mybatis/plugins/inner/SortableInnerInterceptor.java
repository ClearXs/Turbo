package cc.allio.turbo.common.mybatis.plugins.inner;

import cc.allio.turbo.common.constant.Direction;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.turbo.common.mybatis.constraint.Sortable;
import cc.allio.turbo.common.mybatis.help.MybatisKit;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;

/**
 * 针对单实体类型，排序SQL更改
 *
 * @author jiangwei
 * @date 2023/12/7 17:06
 * @see Sortable
 * @since 0.1.0
 */
public class SortableInnerInterceptor extends PaginationInnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if (SqlCommandType.SELECT != sqlCommandType) {
            return;
        }

        Class<?> entityType = null;
        List<ResultMap> resultMaps = ms.getResultMaps();
        if (CollectionUtils.isNotEmpty(resultMaps)) {
            ResultMap resultMap = resultMaps.get(0);
            entityType = resultMap.getType();
        }
        if (entityType != null) {
            List<OrderItem> sortItems = ReflectionKit.getFieldList(entityType)
                    .stream()
                    .filter(field -> field.isAnnotationPresent(Sortable.class))
                    .map(field -> {
                        Sortable sortable = field.getAnnotation(Sortable.class);
                        if (Direction.ASC == sortable.direction()) {
                            return OrderItem.asc(MybatisKit.getTableColumn(field));
                        } else {
                            return OrderItem.desc(MybatisKit.getTableColumn(field));
                        }
                    })
                    .toList();
            String originalSql = boundSql.getSql();
            String buildSql = concatOrderBy(originalSql, sortItems);
            PluginUtils.mpBoundSql(boundSql).sql(buildSql);
        }
    }
}
