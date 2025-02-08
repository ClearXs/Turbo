package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.ai.enums.MessageType;
import cc.allio.turbo.modules.ai.enums.Method;
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
public class AiMessage extends TenantEntity {

    /**
     * 消息类型
     */
    @TableField("type")
    @Schema(description = "消息类型")
    private MessageType type;

    /**
     * role
     */
    @TableField("role")
    @Schema(description = "role")
    private Role role;

    /**
     * method
     */
    @TableField("method")
    @Schema(description = "method")
    private Method method;

    /**
     * content
     */
    @TableField("content")
    @Schema(description = "content")
    private String content;
}
