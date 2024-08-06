package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_template")
@Schema(description = "文档模板")
public class DocTemplate extends TenantEntity {

    /**
     * 上传者
     */
    @TableField("uploader")
    @Schema(name = "上传者", description = "")
    private Long uploader;

    /**
     * 模板名称
     */
    @TableField("title")
    @Schema(name = "模板名称", description = "")
    private String title;

    /**
     * 模板类型
     */
    @TableField("type")
    @Schema(name = "模板类型", description = "")
    private String type;

    /**
     * 文件
     */
    @TableField("file")
    @Schema(name = "文件", description = "")
    private String file;
}
