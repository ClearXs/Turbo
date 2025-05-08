package cc.allio.turbo.modules.ai.chat;

import cc.allio.turbo.modules.ai.chat.instruction.Help;
import cc.allio.turbo.modules.ai.chat.memory.SessionInMemoryChatMemory;
import cc.allio.turbo.modules.ai.chat.message.AdvancedMessage;
import cc.allio.turbo.modules.ai.chat.message.StreamMessage;
import cc.allio.turbo.modules.ai.driver.model.Order;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class ChatServiceTest extends BaseTestCase {


    Order userOrder = Order.toUser("hello");
    Order instructionOrder = Order.toInstruction("/help");

    @Test
    void testCall() {
        ChatService chatService = new ChatService(AgentModel.ollama(), new SessionInMemoryChatMemory("1"));

        // chat with instruction
        chatService.call(instructionOrder)
                .take(1)
                .single()
                .map(AdvancedMessage::content)
                .as(StepVerifier::create)
                .expectNext(Help.HELP_TEXT)
                .verifyComplete();

        // chat with llm
        chatService.call(userOrder)
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();

        // chat wit complex message
        chatService.call(instructionOrder, userOrder)
                .as(StepVerifier::create)
                .expectNextCount(2L)
                .verifyComplete();

    }

    @Test
    void testStream() {
        ChatService chatService = new ChatService(AgentModel.ollama(), new SessionInMemoryChatMemory("1"));

        chatService.stream(instructionOrder)
                .flatMapMany(StreamMessage::observe)
                .take(1)
                .single()
                .map(AdvancedMessage::content)
                .as(StepVerifier::create)
                .expectNext(Help.HELP_TEXT)
                .verifyComplete();


        chatService.stream(instructionOrder)
                .flatMapMany(StreamMessage::observe)
                .take(10)
                .as(StepVerifier::create)
                .expectNextCount(10L)
                .verifyComplete();
    }

}
