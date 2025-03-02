package cc.allio.turbo.modules.ai.model.message;

import cc.allio.turbo.modules.ai.enums.Role;

/**
 * call chat message.
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Message {

    /**
     * message content
     */
    String content();

    /**
     * get finish message
     */
    String finish();

    long createAt();

    Role role();

    String UNFINISH = null;
    String FINISH_STOP = "stop";
    String FINISH_LENGTH = "length";
    String FINISH_CONTENT_FILTER = "content_filter";
    String FINISH_ERROR = "error";
}
