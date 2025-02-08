package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.development.enums.DatasetSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_dataset")
@Schema(description = "数据集")
public class DevDataset extends TenantEntity implements CategoryEntity {

    /**
     * 数据集名称
     */
    @TableField("name")
    @Schema(description = "数据集名称")
    @NotBlank
    private String name;

    /**
     * 数据集编码
     */
    @TableField("code")
    @Schema(description = "数据集编码")
    @NotBlank
    @Unique
    private String code;

    /**
     * 来源
     */
    @TableField("source")
    @Schema(description = "来源")
    @NotBlank
    private DatasetSource source;

    /**
     * 来源id
     */
    @TableField("source_id")
    @Schema(description = "来源id")
    private Long sourceId;

    /**
     * 配置信息
     */
    @TableField("advanced")
    @Schema(description = "配置信息")
    private String advanced;

    /**
     * 分类id
     */
    @TableField("category_id")
    @Schema(description = "分类id")
    private Long categoryId;
}
