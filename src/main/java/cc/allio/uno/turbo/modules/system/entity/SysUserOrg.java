package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("sys_user_org")
@Schema(description = "用户组织关联")
public class SysUserOrg extends IdEntity {

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 组织id
     */
    @TableField("org_id")
    @Schema(description = "组织id")
    private Long orgId;
}
