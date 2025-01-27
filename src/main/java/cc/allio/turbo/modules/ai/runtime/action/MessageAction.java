package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.uno.core.StringPool;
import lombok.Getter;

/**
 * additional resource
 *
 * @author j.x
 * @since 0.2.0
 */
@Getter
public abstract class MessageAction implements Action {

    private String message = StringPool.EMPTY;

    void composeMessage(String content) {
        // TODO string template parse this content
        this.message = content;
    }

}
