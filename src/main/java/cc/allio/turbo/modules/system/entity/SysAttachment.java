package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.extension.oss.Provider;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_attachment")
@Schema(description = "系统附件")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAttachment extends TenantEntity {

    /**
     * 文件名称
     */
    @TableField("filename")
    @Schema(description = "文件名称")
    private String filename;

    /**
     * 文件地址
     */
    @TableField("filepath")
    @Schema(description = "文件地址")
    private String filepath;

    /**
     * 文件大小
     */
    @TableField("filesize")
    @Schema(description = "文件大小")
    private Long filesize;

    /**
     * 文件类型
     */
    @TableField("filetype")
    @Schema(description = "文件类型")
    private String filetype;

    /**
     * 关键附件标识
     */
    @TableField("key")
    @Schema(description = "关键附件标识")
    private String key;

    /**
     * 存储供应商
     */
    @TableField("provider")
    @Schema(description = "存储供应商")
    private Provider provider;
}
