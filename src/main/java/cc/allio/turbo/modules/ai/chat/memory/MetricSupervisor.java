package cc.allio.turbo.modules.ai.chat.memory;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

/**
 * supervision chat with LLM. contains
 *
 * @author j.x
 * @since 0.2.0
 */
public class MetricSupervisor implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return "metric";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain chain) {
        return chain.nextCall(chatClientRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain chain) {
        return chain.nextStream(chatClientRequest);
    }
}
