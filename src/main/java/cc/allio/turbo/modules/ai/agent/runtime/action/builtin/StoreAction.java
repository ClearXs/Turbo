package cc.allio.turbo.modules.ai.agent.runtime.action.builtin;

import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.agent.runtime.action.Action;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.entity.AIMessage;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.enums.Role;
import cc.allio.turbo.modules.ai.store.ChatMessageStore;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * store message action
 *
 * @author j.x
 * @since 0.2.0
 */
@Priority(Integer.MIN_VALUE)
@AllArgsConstructor
public class StoreAction implements Action {

    private final ChatMessageStore chatMessageStore;

    @Override
    public Flux<Output> executeMany(Chain<Environment, Output> chain, ChainContext<Environment> context) throws Throwable {
        Environment environment = context.getIN();
        Input input = environment.getInput();
        String conversationId = input.getConversationId();
        String sessionId = input.getSessionId();
        String agent = input.getAgent();
        ExecutionMode executionMode = input.getExecutionMode();
        Set<Order> instructions = input.getInstructions();

        return Flux.defer(() -> {
                    List<AIMessage> userMessages = Lists.newArrayList();
                    for (Order instruction : instructions) {
                        if (Role.USER.equals(instruction.getRole())) {
                            AIMessage message = new AIMessage();
                            message.setChatId(Long.valueOf(conversationId));
                            message.setSessionId(input.getSessionId());
                            message.setState(MessageStatus.COMPLETE);
                            message.setRole(Role.USER);
                            message.setContent(instruction.getMessage());
                            userMessages.add(message);
                        }
                    }
                    chatMessageStore.saveOrUpdateBatch(userMessages);
                    environment.setUserMessage(userMessages);

                    AIMessage assistantMessage = new AIMessage();
                    assistantMessage.setChatId(Long.valueOf(conversationId));
                    assistantMessage.setSessionId(input.getSessionId());
                    assistantMessage.setRole(Role.ASSISTANT);
                    chatMessageStore.save(assistantMessage);
                    environment.setAssistantMessage(assistantMessage);

                    return Flux.concat(
                            Flux.fromIterable(userMessages),
                            // delay assistant message 100 millis
                            Flux.just(assistantMessage).delayElements(Duration.ofMillis(100L))
                    );
                })
                .map(message -> {
                    Output output = Output.fromAIMessage(message);
                    output.setCreateAt(new Date().getTime());
                    output.setConversationId(conversationId);
                    output.setSessionId(sessionId);
                    output.setAgent(agent);
                    output.setExecutionMode(executionMode);
                    output.setStatus(message.getState());
                    output.setInput(input);
                    output.setMetadata(Maps.newHashMap());
                    return output;
                })
                .concatWith(chain.proceed(context));
    }

    @Override
    public String getName() {
        return "store";
    }
}
