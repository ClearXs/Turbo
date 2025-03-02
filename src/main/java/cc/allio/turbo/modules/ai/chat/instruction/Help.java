package cc.allio.turbo.modules.ai.chat.instruction;

import cc.allio.turbo.modules.ai.chat.ChatService;
import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.MessageImpl;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * system help instruction.
 *
 * @author j.x
 * @since 0.2.0
 */
public class Help implements Instruction {

    private final ChatService chatService;

    public static final String HELP_TEXT = "this is help text";

    public Help(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Mono<StreamMessage> stream(Prompt prompt, Set<FunctionTool> tools) {
        return StreamMessage.from(HELP_TEXT);
    }

    @Override
    public Flux<AdvancedMessage> call(Prompt prompt, Set<FunctionTool> tools) {
        MessageImpl message = new MessageImpl();
        message.setContent(HELP_TEXT);
        return Flux.just(message);
    }

    @Override
    public String name() {
        return "/help";
    }
}
