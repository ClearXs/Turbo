package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.BaseEntity;
import cc.allio.turbo.modules.developer.constant.DataSourceStatus;
import cc.allio.turbo.modules.developer.constant.StorageType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_datasource")
@Schema(description = "数据源")
public class DevDataSource extends BaseEntity {

    /**
     * 数据源名称
     */
    @TableField("name")
    @Schema(description = "数据源名称")
    @NotBlank
    private String name;

    /**
     * 数据源标识
     */
    @TableField("key")
    @Schema(description = "数据源标识")
    @NotBlank
    @Unique
    private String key;

    /**
     * 数据源连接地址
     */
    @TableField("address")
    @Schema(description = "数据源连接地址")
    @NotBlank
    private String address;

    /**
     * 连接用户名
     */
    @TableField("username")
    @Schema(description = "连接用户名")
    private String username;

    /**
     * 连接密码
     */
    @TableField("password")
    @Schema(description = "连接密码")
    private String password;

    /**
     * 连接数据库
     */
    @TableField("database")
    @Schema(description = "连接数据库")
    private String database;

    /**
     * 数据源引擎
     */
    @TableField("engine")
    @Schema(description = "引擎")
    private StorageType engine;

    /**
     * 是否默认的
     */
    @TableField("is_default")
    @Schema(description = "是否默认的")
    private boolean defaulted = false;

    /**
     * 数据源状态
     */
    @TableField("status")
    @Schema(description = "数据源状态")
    private DataSourceStatus status;
}
