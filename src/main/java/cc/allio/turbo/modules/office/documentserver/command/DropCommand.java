package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.documentserver.command.requestinfo.DropArgs;

/**
 * {@link Method#drop} command
 *
 * @author j.x
 * @date 2024/5/27 17:59
 * @since 0.2
 */
public class DropCommand extends Command<DropArgs> {

    public DropCommand(InternalCommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public Result execute(String docKey, DropArgs params) {
        return commandExecutor.doExecute(Method.drop, docKey, formatParams(params));
    }

    @Override
    public Method getMethod() {
        return Method.drop;
    }
}
