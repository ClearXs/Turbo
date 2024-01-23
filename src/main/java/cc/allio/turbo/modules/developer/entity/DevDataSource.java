package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
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
public class DevDataSource extends TenantEntity {

    /**
     * 数据源名称
     */
    @TableField("name")
    @Schema(description = "数据源名称")
    @NotBlank
    private String name;

    /**
     * 数据源连接地址
     */
    @TableField("url")
    @Schema(description = "数据源连接地址")
    @NotBlank
    private String url;

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
     * 数据源引擎
     */
    @TableField("engine")
    @Schema(description = "引擎")
    private String engine;
}
