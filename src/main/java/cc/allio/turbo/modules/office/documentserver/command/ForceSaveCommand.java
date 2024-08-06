package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.documentserver.command.requestinfo.ForceSaveArgs;

/**
 * {@link Method#forcesave} command
 *
 * @author j.x
 * @date 2024/5/13 23:47
 * @since 0.0.1
 */
public class ForceSaveCommand extends Command<ForceSaveArgs> {

    public ForceSaveCommand(InternalCommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public Result execute(String docKey, ForceSaveArgs params) {
        return commandExecutor.doExecute(Method.forcesave, docKey, formatParams(params));
    }

    @Override
    public Method getMethod() {
        return Method.forcesave;
    }
}
