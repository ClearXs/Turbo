package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("sys_role_menu")
@Schema(description = "角色菜单关联")
public class SysRoleMenu extends IdEntity {

    /**
     * 角色id
     */
    @TableField("role_id")
    @Schema(description = "角色Id")
    private Long roleId;

    /**
     * 用户id
     */
    @TableField("menu_id")
    @Schema(description = "菜单Id")
    private Long menuId;
}
