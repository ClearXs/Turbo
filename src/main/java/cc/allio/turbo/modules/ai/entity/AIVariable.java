package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_variable")
@Schema(description = "变量")
public class AIVariable extends TenantEntity {

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
     * 值
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String value;

    /**
     * 类型
     */
    @TableField("type")
    @Schema(description = "类型")
    private String type;

    /**
     * 描述
     */
    @TableField("description")
    @Schema(description = "描述")
    private String description;
}
