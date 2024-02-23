package cc.allio.turbo.common.db.uno.interceptor;

import cc.allio.uno.core.function.lambda.MethodBiConsumer;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * 租户拦截器。基于MyBatis{@link TenantLineHandler}
 *
 * @author jiangwei
 * @date 2024/2/17 14:04
 * @since 1.1.6
 */
@AllArgsConstructor
public class TenantInterceptor implements Interceptor {

    private final TenantLineHandler tenantLineHandler;

    /**
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    @Override
    public void onQueryBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof QueryOperator queryOperator) {
            Table table = queryOperator.getTable();
            allowTenant(table, queryOperator::eq);
        }
    }

    /**
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    @Override
    public void onSaveBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof InsertOperator insertOperator) {
            Table table = insertOperator.getTable();
            allowTenant(table, insertOperator::strictFill);
        }
    }

    /**
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    @Override
    public void onUpdateBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof UpdateOperator updateOperator) {
            Table table = updateOperator.getTable();
            allowTenant(table, updateOperator::strictFill);
        }
    }

    /**
     * 根据给定的{@link Table}判断是否允许给定租户信息进行回调
     *
     * @param table          table
     * @param tenantConsumer 两个参数，参数1是租户字段名称，参数2是租户值
     */
    private void allowTenant(Table table, MethodBiConsumer<String, String> tenantConsumer) {
        String tableName = table.getName().format();
        Expression tenantIdExpression = tenantLineHandler.getTenantId();
        boolean ignored = tenantLineHandler.ignoreTable(tableName);
        String tenantIdColumn = tenantLineHandler.getTenantIdColumn();
        if (!ignored && tenantIdExpression instanceof StringValue tenantId) {
            tenantConsumer.accept(tenantIdColumn, tenantId.getValue());
        }
    }
}
