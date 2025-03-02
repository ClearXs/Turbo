package cc.allio.turbo.modules.ai.driver;

import cc.allio.uno.core.bus.TopicKey;

public interface Topics {

    /**
     * subscribe user input
     * <p>
     * typically user:chat/input/{conversationId}/{sessionId}
     */
    TopicKey USER_CHAT_INPUT = TopicKey.of("user:chat/input");

    /**
     * subscribe users input (according input id)
     */
    TopicKey USER_CHAT_INPUT_PATTERNS = TopicKey.of("user:chat/input/**");

    /**
     * results evaluation
     */
    TopicKey EVALUATION = TopicKey.of("evaluation");

    /**
     * results evaluation (according input id)
     */
    TopicKey EVALUATION_PATTERNS = TopicKey.of("evaluation/**");

    /**
     * subscribe output
     */
    TopicKey USER_CHAT_OUTPUT = TopicKey.of("user:chat/output");

    /**
     * subscribe output (according output id)
     */
    TopicKey USER_CHAT_OUTPUT_PATTERNS = TopicKey.of("user:chat/output/**");

}
