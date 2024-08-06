package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.turbo.modules.office.documentserver.command.requestinfo.MetaArgs;

/**
 * {@link Method#meta} command
 *
 * @author j.x
 * @date 2024/5/13 22:53
 * @since 0.0.1
 */
public class MetaCommand extends Command<MetaArgs> {

    public MetaCommand(InternalCommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public Result execute(String docKey, MetaArgs params) {
        return commandExecutor.doExecute(Method.meta, docKey, formatParams(params));
    }

    @Override
    public Method getMethod() {
        return Method.meta;
    }
}
