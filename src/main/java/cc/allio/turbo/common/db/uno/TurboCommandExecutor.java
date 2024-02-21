package cc.allio.turbo.common.db.uno;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * {@link CommandExecutor}装饰器，基于{@link cc.allio.uno.data.orm.dsl.type.DBType}获取对应的{@link CommandExecutor}
 *
 * @author jiangwei
 * @date 2024/1/25 00:37
 * @since 0.1.0
 */
public class TurboCommandExecutor implements CommandExecutor {

    private final CommandExecutor innerCommandExecutor;

    public TurboCommandExecutor(DBType dbType) {
        if (dbType == null) {
            throw new NullPointerException("offer db type");
        }
        DBType.DBCategory category = dbType.getCategory();
        if (DBType.DBCategory.RELATIONAL == category) {
            this.innerCommandExecutor = CommandExecutorFactory.getDSLExecutor(ExecutorKey.DB);
        } else {
            this.innerCommandExecutor = CommandExecutorFactory.getDSLExecutor(ExecutorKey.returnKey(dbType.getName()));
        }
        if (innerCommandExecutor == null) {
            throw new IllegalArgumentException(String.format("according to %s can't get command executor, " +
                    "check corresponding 'command executor' without CommandFactory#regitr", category));
        }
    }

    public TurboCommandExecutor(CommandExecutor commandExecutor) {
        this.innerCommandExecutor = commandExecutor;
    }

    @Override
    public boolean bool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        return innerCommandExecutor.bool(operator, commandType, resultSetHandler);
    }

    @Override
    public <R> List<R> queryList(QueryOperator queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        return innerCommandExecutor.queryList(queryOperator, commandType, resultSetHandler);
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        return innerCommandExecutor.check();
    }

    @Override
    public ExecutorKey getKey() {
        return innerCommandExecutor.getKey();
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return innerCommandExecutor.getOperatorGroup();
    }

    @Override
    public ExecutorOptions getOptions() {
        return innerCommandExecutor.getOptions();
    }
}
