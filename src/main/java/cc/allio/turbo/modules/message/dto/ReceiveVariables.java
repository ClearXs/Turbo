package cc.allio.turbo.modules.message.dto;

import cc.allio.turbo.modules.message.template.TemporaryTemplate;
import lombok.Data;


@Data
public class ReceiveVariables {

    /**
     * 消息配置key
     */
    private String configKey;

    /**
     * temporary template
     */
    private TemporaryTemplate temporary;
}
