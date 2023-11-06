package cc.allio.uno.turbo.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_menu")
@Schema(description = "系统菜单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends TenantEntity {

    /**
     * 菜单编码
     */
    @TableField("code")
    @Schema(description = "菜单编码")
    private String code;

    /**
     * 菜单名称
     */
    @TableField("name")
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单序号
     */
    @TableField("sort")
    @Schema(description = "菜单序号")
    private String sort;

    /**
     * 父级菜单
     */
    @TableField("parent_id")
    @Schema(description = "父级菜单")
    private Long parentId;

    /**
     * 别名
     */
    @TableField("alias")
    @Schema(description = "别名")
    private String alias;

    /**
     * 路径
     */
    @TableField("path")
    @Schema(description = "路径")
    private String path;

    /**
     * icon
     */
    @TableField("icon")
    @Schema(description = "icon")
    private String icon;

    /**
     * 权限
     */
    @TableField("permission")
    @Schema(description = "permission")
    private String permission;
}
