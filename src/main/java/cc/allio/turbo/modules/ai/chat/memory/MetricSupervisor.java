package cc.allio.turbo.modules.ai.chat.memory;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

/**
 * supervision chat with LLM. contains
 *
 * @author j.x
 * @since 0.2.0
 */
public class MetricSupervisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public String getName() {
        return "metric";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(advisedRequest);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(advisedRequest);
    }
}
