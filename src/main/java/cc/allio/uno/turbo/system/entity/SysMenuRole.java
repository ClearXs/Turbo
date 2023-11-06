package cc.allio.uno.turbo.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

@Data
@TableName("sys_menu_role")
@Schema(description = "菜单角色关联")
public class SysMenuRole {

    /**
     * 主键
     */
    @TableId(type = ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 用户id
     */
    @TableField("menu_id")
    @Schema(description = "角色id")
    private Long menuId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @Schema(description = "角色id")
    private Long roleId;
}
