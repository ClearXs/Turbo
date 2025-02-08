package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.ai.enums.Behavior;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_action")
@Schema(description = "action")
public class AiAction extends TenantEntity {

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 消息
     */
    @TableField("message")
    @Schema(description = "消息")
    private String message;

    /**
     * behavior
     */
    @TableField("behavior")
    @Schema(description = "行为")
    private Behavior behavior;

    /**
     * 触发动作 json data.
     */
    @TableField("trigger")
    @Schema(description = "触发动作")
    private Map<String, Object> trigger;

    /**
     * 是否内置
     */
    @TableField("built_in")
    @Schema(description = "是否内置")
    private Boolean builtIn;
}
