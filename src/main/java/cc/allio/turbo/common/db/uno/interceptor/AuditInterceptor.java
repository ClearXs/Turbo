package cc.allio.turbo.common.db.uno.interceptor;

import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;

import java.util.Date;

/**
 * db 审计拦截器
 *
 * @author j.x
 * @date 2024/2/16 23:50
 * @since 1.1.6
 */
public class AuditInterceptor implements Interceptor {

    @Override
    public void onSaveBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof InsertOperator insertOperator) {
            Date now = DateUtil.now();
            Long currentUserId = AuthUtil.getUserId();
            insertOperator.strictFill("created_by", currentUserId);
            insertOperator.strictFill("created_time", now);
            insertOperator.strictFill("updated_by", currentUserId);
            insertOperator.strictFill("updated_time", now);
            insertOperator.strictFill("is_deleted", 0);
        }
    }

    @Override
    public void onUpdateBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof UpdateOperator updateOperator) {
            Date now = DateUtil.now();
            Long currentUserId = AuthUtil.getUserId();
            updateOperator.strictFill("updated_by", currentUserId);
            updateOperator.strictFill("updated_time", now);
            updateOperator.strictFill("is_deleted", 0);
        }
    }
}
