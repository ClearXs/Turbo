package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.documentserver.command.requestinfo.EmptyArgs;

/**
 * {@link Method#info} command
 *
 * @author j.x
 * @date 2024/5/27 17:40
 * @since 0.2
 */
public class InfoCommand extends Command<EmptyArgs> {

    public InfoCommand(InternalCommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public Result execute(String docKey, EmptyArgs params) {
        return commandExecutor.doExecute(Method.info, docKey, null);
    }

    @Override
    public Method getMethod() {
        return Method.info;
    }
}
