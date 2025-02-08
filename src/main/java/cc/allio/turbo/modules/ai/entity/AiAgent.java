package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_agent")
@Schema(description = "agent")
public class AiAgent extends TenantEntity {

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    @Schema(description = "描述")
    private String description;

    /**
     * 系统 prompt
     */
    @TableField("prompt")
    @Schema(description = "系统 prompt")
    private String prompt;

    /**
     * actions List of String
     */
    @TableField("actions")
    @Schema(description = "actions List of String")
    private List<String> actions;

    /**
     * tools json data
     */
    @TableField("tools")
    @Schema(description = "tools json data")
    private Map<String, Object> tools;

    /**
     * tools List of String
     */
    @TableField("external_tools")
    @Schema(description = "external tools List of String")
    private List<String> externalTools;

    /**
     * 是否内置
     */
    @TableField("built_in")
    @Schema(description = "是否内置")
    private Boolean builtIn;
}
