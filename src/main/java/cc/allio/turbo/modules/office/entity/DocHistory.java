package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_history")
@Schema(description = "文档历史版本")
public class DocHistory extends TenantEntity {

    /**
     * 文档ID
     */
    @TableField("doc_id")
    @Schema(name = "文档ID")
    private Long docId;

    /**
     * 文档名称
     */
    @TableField("doc_title")
    @Schema(name = "文档名称")
    private String docTitle;

    /**
     * 文档类型
     */
    @TableField("doc_type")
    @Schema(name = "文档类型")
    private String docType;

    /**
     * ONLYOFFICE唯一标识
     */
    @TableField("doc_key")
    @Schema(name = "文档唯一标识", description = "ONLYOFFICE唯一标识")
    private String docKey;

    /**
     * 文件数据，附件JSON array数据
     */
    @TableField("file")
    @Schema(name = "文件数据", description = "附件JSON array数据")
    private String file;

    /**
     * 版本号
     */
    @TableField("doc_version")
    @Schema(name = "版本号")
    private Integer docVersion;

    /**
     * 维护用户ID
     */
    @TableField("maintain_user_id")
    @Schema(name = "维护用户ID")
    private Long maintainUserId;

    /**
     * 维护用户名称
     */
    @TableField("maintain_user_name")
    @Schema(name = "维护用户名称")
    private String maintainUsername;

    /**
     * 变更url
     */
    @TableField("change_url")
    @Schema(name = "变更链接")
    private String changeUrl;

    /**
     * onlyoffice server 版本号
     */
    @TableField("server_version")
    @Schema(name = "onlyoffice server 版本号")
    private String serverVersion;

    /**
     * changes
     */
    @TableField("changes")
    @Schema(name = "onlyoffice changes数据")
    private String changes;
}
