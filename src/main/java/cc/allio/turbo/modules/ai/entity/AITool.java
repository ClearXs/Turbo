package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_tool")
@Schema(description = "tool")
public class AITool extends TenantEntity {

    /**
     * 名称
     */
    @TableField("key")
    @Schema(description = "key")
    @NotBlank
    private String key;

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
     * 参数 json schema
     */
    @TableField("parameters")
    @Schema(description = "参数 json schema")
    private Map<String, Object> parameters;
}
