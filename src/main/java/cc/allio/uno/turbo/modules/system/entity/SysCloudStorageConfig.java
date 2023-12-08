package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.BaseEntity;
import cc.allio.uno.turbo.modules.system.constant.CsType;
import cc.allio.uno.turbo.extension.oss.Provider;
import cc.allio.uno.turbo.common.constant.Enable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统级别组件，不应该让其他租户也进行使用
 *
 * @author j.x
 * @date 2023/11/18 10:24
 * @since 0.1.0
 */
@TableName("sys_cloud_storage_config")
@Schema(description = "系统附件")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysCloudStorageConfig extends BaseEntity {

    /**
     * 文件名称
     */
    @TableField("name")
    @Schema(description = "云存储配置名称")
    private String name;

    /**
     * 云存储端点
     */
    @TableField("endpoint")
    @Schema(description = "云存储端点")
    private String endpoint;

    /**
     * 云存储供应商
     */
    @TableField("provider")
    @Schema(description = "云存储供应商")
    private Provider provider;

    /**
     * 云存储供应商
     */
    @TableField("cs_type")
    @Schema(description = "云存储类型")
    private CsType csType;

    /**
     * 存储空间
     */
    @TableField("bucket")
    @Schema(description = "存储空间")
    private String bucket;

    /**
     * 访问ID
     */
    @TableField("access_id")
    @Schema(description = "访问ID")
    private String accessId;

    /**
     * 访问密钥
     */
    @TableField("access_key")
    @Schema(description = "访问密钥")
    private String accessKey;

    /**
     * 是否启用
     */
    @TableField("enable")
    @Schema(description = "是否启用")
    private Enable enable;
}
