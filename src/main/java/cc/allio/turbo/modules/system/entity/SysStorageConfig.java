package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.BaseEntity;
import cc.allio.turbo.modules.system.enums.StorageType;
import cc.allio.turbo.extension.oss.Provider;
import cc.allio.turbo.common.constant.Enable;
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
@TableName("sys_storage_config")
@Schema(description = "系统存储配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysStorageConfig extends BaseEntity {

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
    private StorageType csType;

    /**
     * 存储空间
     */
    @TableField("bucket")
    @Schema(description = "存储空间")
    private String bucket;

    /**
     * 访问ID
     */
    @TableField("access_key")
    @Schema(description = "访问Key")
    private String accessKey;

    /**
     * 访问密钥
     */
    @TableField("secret_key")
    @Schema(description = "访问密钥")
    private String secretKey;

    /**
     * 是否启用
     */
    @TableField("enable")
    @Schema(description = "是否启用")
    private Enable enable;
}
