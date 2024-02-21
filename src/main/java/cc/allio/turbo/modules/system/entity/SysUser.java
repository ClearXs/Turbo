package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.system.constant.UserStatus;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @ColumnWidth(25)
    @ExcelProperty(value = "用户名", index = 0)
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    @Schema(description = "密码")
    @NotBlank
    @ColumnWidth(25)
    @ExcelProperty(value = "密码", index = 1)
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(description = "邮箱")
    @ColumnWidth(25)
    @ExcelProperty(value = "邮箱", index = 2)
    private String email;

    /**
     * 电话号码
     */
    @TableField("phone")
    @Schema(description = "电话号码")
    @ColumnWidth(25)
    @ExcelProperty(value = "电话号码", index = 3)
    private String phone;

    /**
     * 用户状态
     */
    @TableField("status")
    @Schema(description = "用户状态")
    @ColumnWidth(25)
    @ExcelProperty(value = "用户状态",index = 4)
    private UserStatus status;

    /**
     * 头像
     */
    @TableField("avatar")
    @Schema(description = "头像")
    @ColumnWidth(25)
    @ExcelProperty(value = "头像", index = 5)
    private String avatar;

    /**
     * 昵称
     */
    @TableField("nickname")
    @Schema(description = "昵称")
    @ColumnWidth(25)
    @ExcelProperty(value = "昵称", index = 6)
    private String nickname;

    /**
     * 所属组织
     */
    @TableField("org_id")
    @Schema(description = "所属组织")
    @ColumnWidth(25)
    @ExcelProperty(value = "所属组织", index = 7)
    private Long orgId;
}
