package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_chat")
@Schema(description = "chat")
public class AIChat extends TenantEntity {

    /**
     * ws session
     */
    @TableField("session_id")
    @Schema(description = "session id")
    private String sessionId;
}
