package cc.allio.turbo.common.db.mybatis.plugins.inner;

import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.util.CollectionUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link Unique}的实现
 *
 * @author j.x
 * @date 2023/11/23 11:25
 * @since 0.1.0
 */
public class ConstraintInnerInterceptor implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if (SqlCommandType.INSERT != sqlCommandType) {
            return;
        }
        ParameterMap parameterMap = ms.getParameterMap();
        Class<?> type = parameterMap.getType();
        BaseMapper mapper = null;
        try {
            mapper = SqlHelper.getMapper(type, SqlHelper.sqlSession(type));
        } catch (Throwable ex) {
            // ignore
        }

        if (mapper == null) {
            return;
        }

        // 唯一性判断
        List<Field> constraints =
                ReflectionKit.getFieldList(type)
                        .stream()
                        .filter(field -> field.isAnnotationPresent(Unique.class))
                        .toList();

        QueryWrapper<?> queryWrapper = getQueryWrapper(parameter, constraints);
        if (queryWrapper != null) {
            Long count = mapper.selectCount(queryWrapper);
            if (count > 0) {
                printError(constraints);
            }
        }
    }

    private static QueryWrapper<?> getQueryWrapper(Object parameter, List<Field> constraints) {
        if (CollectionUtils.isEmpty(constraints)) {
            return null;
        }
        BeanWrapper beanWrapper = null;
        if (parameter instanceof Map<?, ?> map) {
            Object entity = map.getOrDefault(Constants.ENTITY, null);
            if (entity != null) {
                beanWrapper = new BeanWrapper(entity);
            }
        } else {
            beanWrapper = new BeanWrapper(parameter);
        }
        if (beanWrapper == null) {
            return null;
        }
        QueryWrapper<?> queryWrapper = Wrappers.query();
        for (Field constraint : constraints) {
            Object value = beanWrapper.getForce(constraint.getName());
            String column = constraint.getName();
            if (constraint.isAnnotationPresent(TableField.class)) {
                TableField tableField = constraint.getAnnotation(TableField.class);
                column = tableField.value();
            }
            queryWrapper.eq(value != null, column, value);
        }
        return queryWrapper;
    }

    private void printError(List<Field> constraints) throws SQLException {
        String errorMsg = constraints.stream()
                .map(constraint -> {
                    Schema schema = constraint.getAnnotation(Schema.class);
                    if (schema != null) {
                        return schema.name();
                    } else {
                        return constraint.getName();
                    }
                })
                .collect(Collectors.joining(StringPool.COMMA));
        throw new SQLException(errorMsg + " Violate the principle of uniqueness");
    }
}
