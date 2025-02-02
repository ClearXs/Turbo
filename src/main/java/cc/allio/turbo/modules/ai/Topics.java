package cc.allio.turbo.modules.ai;

import cc.allio.uno.core.bus.TopicKey;

public interface Topics {

    /**
     * subscribe user input
     */
    TopicKey USER_INPUT = TopicKey.of("user:input");

    /**
     * subscribe users input (according input id)
     */
    TopicKey USER_INPUT_PATTERNS = TopicKey.of("user:input/**");

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
    TopicKey OUTPUT = TopicKey.of("output");

    /**
     * subscribe output (according output id)
     */
    TopicKey OUTPUT_PATTERNS = TopicKey.of("output/**");

}
