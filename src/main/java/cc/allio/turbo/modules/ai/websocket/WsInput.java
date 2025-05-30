package cc.allio.turbo.modules.ai.websocket;

import cc.allio.turbo.modules.ai.driver.model.Input;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket for input
 *
 * @author j.x
 * @since 0.2.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsInput extends Input {

    private WebSocketSession session;
}
