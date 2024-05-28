package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.constant.CodeTemplateDomain;
import cc.allio.turbo.modules.developer.constant.CodeTemplateLanguage;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成实例
 *
 * @author j.x
 * @date 2024/5/2 17:49
 * @since 0.1.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_code_generate")
@Schema(description = "代码生成")
public class DevCodeGenerate extends TenantEntity implements CategoryEntity {

    /**
     * 模板集合
     */
    @TableField("template_category_id")
    @Schema(description = "模板集合")
    private Long templateCategoryId;

    /**
     * 模块名称
     */
    @TableField("module_name")
    @Schema(description = "模块名称")
    private String moduleName;

    /**
     * 模块Key
     */
    @TableField("module_key")
    @Schema(description = "模块Key")
    private String moduleKey;

    /**
     * 模块包路径
     */
    @TableField("module_package_path")
    @Schema(description = "模块包路径")
    private String modulePackagePath;

    /**
     * 模块请求路径
     */
    @TableField("module_request_path")
    @Schema(description = "模块请求路径")
    private String moduleRequestPath;

    /**
     * 作者
     */
    @TableField("module_version")
    @Schema(description = "模块版本号")
    private String moduleVersion;

    /**
     * 作者
     */
    @TableField("module_author")
    @Schema(description = "作者")
    private String moduleAuthor;

    /**
     * 所属子系统KEY
     */
    @TableField("system")
    @Schema(description = "所属子系统KEY")
    private String system;

    /**
     * 来源;实体表、数据集
     */
    @TableField("source")
    @Schema(description = "来源")
    private CodeGenerateSource source;

    /**
     * 数据集ID
     */
    @TableField("page_id")
    @Schema(description = "页面id")
    private Long pageId;

    /**
     * 实体表;实体表实体（json数据）
     */
    @TableField("datatable")
    @Schema(description = "实体表实体（json数据）")
    private String datatable;

    /**
     * 数据视图;json数据
     */
    @TableField("data_view")
    @Schema(description = "数据视图")
    private String dataView;

    /**
     * 分类ID;子系统分类
     */
    @TableField("category_id")
    @Schema(description = "分类ID")
    private Long categoryId;
}
