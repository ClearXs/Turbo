package cc.allio.turbo.modules.ai.api.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_chat_session")
@Schema(description = "chat")
public class AIChatSession extends TenantEntity {

    @TableField("chat_id")
    @Schema(description = "chat id")
    private Long chatId;
}
