package cc.allio.turbo.modules.ai.task.action.internal;

import cc.allio.turbo.modules.ai.task.Response;
import cc.allio.turbo.modules.ai.task.TaskContext;
import cc.allio.turbo.modules.ai.task.action.MessageAction;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import reactor.core.publisher.Mono;

public class CreateAction extends MessageAction {

    @Override
    public String message() {
        return "";
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Mono<Response> execute(Chain<TaskContext, Response> chain, ChainContext<TaskContext> context) throws Throwable {
        return null;
    }
}
