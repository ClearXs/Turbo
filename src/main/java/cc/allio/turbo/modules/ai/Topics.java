package cc.allio.turbo.modules.ai;

import cc.allio.uno.core.bus.TopicKey;

public interface Topics {

    TopicKey USER_INPUT = TopicKey.of("user:input");

    /**
     * subscribe users input (according input id)
     */
    TopicKey USER_INPUT_PATTERNS = TopicKey.of("user:input/**");

    /**
     * subscribe users evaluation (according input id)
     */
    TopicKey EVALUATION_PATTERNS = TopicKey.of("evaluation/**");

}
