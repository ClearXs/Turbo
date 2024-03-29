package cc.allio.turbo.common.db.uno.interceptor;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;

/**
 * 逻辑删除拦截器
 *
 * @author j.x
 * @date 2024/2/16 23:50
 * @since 1.1.6
 */
public class LogicInterceptor implements Interceptor {

    @Override
    public void onDeleteBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof UpdateOperator updateOperator) {
            updateOperator.update("is_deleted", 1);
        }
    }

    @Override
    public void onQueryBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof QueryOperator queryOperator) {
            queryOperator.eq("is_deleted", 0);
        }
    }
}
