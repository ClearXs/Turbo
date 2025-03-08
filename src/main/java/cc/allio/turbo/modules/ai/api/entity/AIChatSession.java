package cc.allio.turbo.modules.ai.api.entity;

import cc.allio.turbo.common.db.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

@Data
@TableName("ai_chat_session")
@Schema(description = "chat session")
public class AIChatSession implements Entity {

    /**
     * 主键
     */
    @TableId(type = ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    @TableField("chat_id")
    @Schema(description = "chat id")
    private Long chatId;

    @TableField("user_id")
    @Schema(description = "user id")
    private Long userId;

}
