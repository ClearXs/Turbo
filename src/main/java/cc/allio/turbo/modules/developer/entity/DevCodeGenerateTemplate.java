package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.developer.constant.CodeTemplateDomain;
import cc.allio.turbo.modules.developer.constant.CodeTemplateLanguage;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成模板
 *
 * @author j.x
 * @date 2024/5/2 17:49
 * @since 0.1.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_code_generate_template")
@Schema(description = "代码生成模板")
public class DevCodeGenerateTemplate extends TenantEntity implements CategoryEntity {

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 模板内容
     */
    @TableField("content")
    @Schema(description = "模板内容")
    @NotBlank
    private String content;

    /**
     * 模板语言
     */
    @TableField("language")
    @Schema(description = "模板语言")
    private CodeTemplateLanguage language;

    /**
     * 分类id
     */
    @TableField("category_id")
    @Schema(description = "分类id")
    private Long categoryId;

    /**
     * 模板领域
     */
    @TableField("domain")
    @Schema(description = "模板领域")
    private CodeTemplateDomain domain;

    /**
     * 文件名
     */
    @TableField("file_name")
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件路径
     */
    @TableField("file_path")
    @Schema(description = "文件路径")
    private String filePath;
}
