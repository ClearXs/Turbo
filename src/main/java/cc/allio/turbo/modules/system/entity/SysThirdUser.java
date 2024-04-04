package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO
 *
 * @author j.x
 * @date 2024/3/30 17:24
 * @since 0.1.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_third_user")
@Schema(description = "第三方用户")
public class SysThirdUser extends TenantEntity {

    /**
     * 三方系统唯一ID
     */
    @TableField("uuid")
    @Schema(description = "三方系统唯一ID")
    private String uuid;

    /**
     * 三方系统来源code
     */
    @TableField("code")
    @Schema(description = "三方系统来源code")
    private String code;

    /**
     * 系统用户ID
     */
    @TableField("user_id")
    @Schema(description = "系统用户ID")
    private Long userId;
}
