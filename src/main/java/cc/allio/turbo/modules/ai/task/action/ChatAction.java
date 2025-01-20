package cc.allio.turbo.modules.ai.task.action;

import cc.allio.turbo.modules.ai.task.Response;
import cc.allio.turbo.modules.ai.task.TaskContext;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ChatAction extends MessageAction {

    @Override
    public Mono<Response> execute(Chain<TaskContext, Response> chain, ChainContext<TaskContext> context) throws Throwable {
        TaskContext taskContext = context.getIN();
        ChatModel chatModel = taskContext.getChatModel();
        Prompt prompt = taskContext.getPrompt();
        return chatModel
                .stream(prompt)
                .collectList()
                .flatMap(responses -> {
                    return chain.proceed(context);
                });
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String getName() {
        return "chat";
    }
}
