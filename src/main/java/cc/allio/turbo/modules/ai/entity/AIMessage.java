package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.ai.enums.MessageStatus;
import cc.allio.turbo.modules.ai.enums.Role;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_message")
@Schema(description = "消息")
public class AIMessage extends TenantEntity {

    @TableField("chat_id")
    @Schema(description = "chat id")
    private Long chatId;

    @TableField("session_id")
    @Schema(description = "session id")
    private String sessionId;

    /**
     * role
     */
    @TableField("role")
    @Schema(description = "role")
    private Role role;

    /**
     * content
     */
    @TableField("content")
    @Schema(description = "content")
    private String content;

    @TableField("state")
    @Schema(description = "state")
    private MessageStatus state;

    @TableField("error_msg")
    @Schema(description = "error_msg")
    private String errorMsg;
}
