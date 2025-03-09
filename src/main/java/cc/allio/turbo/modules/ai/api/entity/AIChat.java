package cc.allio.turbo.modules.ai.api.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.driver.model.Options;
import cc.allio.turbo.modules.ai.model.ModelOptions;
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
    @TableField("user_id")
    @Schema(description = "user id")
    private Long userId;

    /**
     * select agent
     */
    @TableField("agent")
    private String agent;

    /**
     * model options
     */
    @TableField("model_options")
    private ModelOptions modelOptions;

    /**
     * execution mode
     */
    @TableField("execution_mode")
    private ExecutionMode executionMode;

    /**
     * options
     */
    @TableField("options")
    private Options options;
}
