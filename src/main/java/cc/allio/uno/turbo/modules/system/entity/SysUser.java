package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.TenantEntity;
import cc.allio.uno.turbo.modules.system.constant.UserStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 *
 * @author j.x
 * @date 2023/10/22 11:54
 * @since 0.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
@Schema(description = "系统用户")
public class SysUser extends TenantEntity {

    /**
     * 用户名
     */
    @TableField("username")
    @Schema(description = "用户名")
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    @Schema(description = "密码")
    @NotBlank
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 电话号码
     */
    @TableField("phone")
    @Schema(description = "电话号码")
    private String phone;

    /**
     * 用户状态
     */
    @TableField("status")
    @Schema(description = "用户状态")
    private UserStatus status;

    /**
     * 头像
     */
    @TableField("avatar")
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @TableField("nickname")
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 所属组织
     */
    @TableField("org_id")
    @Schema(description = "所属组织")
    private Long orgId;
}
