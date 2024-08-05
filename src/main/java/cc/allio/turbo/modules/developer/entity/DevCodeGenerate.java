package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.CategoryEntity;
import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "模板集")
    private Long templateCategoryId;

    /**
     * 模块名称
     */
    @TableField("instance_name")
    @Schema(description = "实例名称")
    private String instanceName;

    /**
     * 模块Key
     */
    @TableField("instance_key")
    @Schema(description = "模块Key")
    private String instanceKey;

    /**
     * 模块请求路径
     */
    @TableField("request_path")
    @Schema(description = "实例请求路径")
    private String requestPath;

    /**
     * 作者
     */
    @TableField("instance_version")
    @Schema(description = "实例版本号")
    private String instanceVersion;

    /**
     * 作者
     */
    @TableField("author")
    @Schema(description = "作者")
    private String author;

    /**
     * 来源;实体表、数据集
     */
    @TableField("source")
    @Schema(description = "来源")
    private CodeGenerateSource source;

    /**
     * 数据集ID
     */
    @TableField("bo_id")
    @Schema(description = "BO对象id")
    private Long boId;

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

    /**
     * 是否忽略常用字段
     */
    @TableField("ignore_default_field")
    @Schema(description = "是否忽略常用字段")
    private Boolean ignoreDefaultField;
}
