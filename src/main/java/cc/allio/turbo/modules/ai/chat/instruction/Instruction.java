package cc.allio.turbo.modules.ai.chat.instruction;

import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import jakarta.annotation.Nullable;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * built-in instruction for chat.
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Instruction {

    /**
     * withStream chat.
     *
     * @param prompt the chat prompt
     * @param tools  the list of tools
     * @return withStream of chat response
     */
    Mono<StreamMessage> stream(@Nullable Prompt prompt, Set<FunctionTool> tools);


    /**
     * withCall chat.
     *
     * @param prompt the chat prompt
     * @param tools  the list of tools
     * @return chat response
     */
    Flux<AdvancedMessage> call(@Nullable Prompt prompt, Set<FunctionTool> tools);

    /**
     * obtain the name of the instruction.
     */
    String name();

}
