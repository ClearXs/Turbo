package cc.allio.turbo.modules.ai.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.ai.api.enums.Capability;
import cc.allio.turbo.modules.ai.enums.ModelType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_model")
@Schema(description = "model")
public class AIModel extends TenantEntity {

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 名称
     */
    @TableField("manufacturer_id")
    @Schema(description = "厂商ID")
    private Long manufacturerId;

    /**
     * 是否内置
     */
    @TableField("built_id")
    @Schema(description = "是否内置")
    private Boolean builtIn;

    /**
     *
     */
    @TableField("quantity")
    @Schema(description = "参数量")
    private String quantity;

    /**
     * 模型能力
     */
    @TableField("capabilities")
    @Schema(description = "模型能力")
    private List<Capability> capabilities;

    /**
     * readme
     */
    @TableField("readme")
    @Schema(description = "readme")
    private String readme;
}
