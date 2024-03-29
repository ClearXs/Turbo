package cc.allio.turbo.modules.message.runtime;

import cc.allio.turbo.modules.message.config.MessageConfig;
import cc.allio.turbo.modules.message.entity.SysMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuntimeMessage {

    /**
     * 接收实体
     */
    private ReceiveMetadata receive;

    /**
     * 消息实体
     */
    private SysMessage message;

    /**
     * 消息配置
     */
    private MessageConfig messageConfiguration;

    /**
     * 运行时消息模板
     */
    private RuntimeMessageTemplate messageTemplate;
}
