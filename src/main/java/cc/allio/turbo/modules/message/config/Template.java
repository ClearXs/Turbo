package cc.allio.turbo.modules.message.config;

import cc.allio.turbo.modules.message.template.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息发送模板
 *
 * @author j.x
 * @date 2024/3/29 00:19
 * @since 0.1.1
 */
@Data
@AllArgsConstructor
public class Template {

    private final SendWay sendWay;
    private final String sendKey;
    private final MessageTemplate messageTemplate;
    private final Protocol[] protocols;
}
