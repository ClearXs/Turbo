package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.core.datastructure.tree.Expand;
import cc.allio.uno.turbo.common.mybatis.entity.TenantEntity;
import cc.allio.uno.turbo.modules.system.constant.MenuType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_menu")
@Schema(description = "系统菜单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends TenantEntity implements Expand {

    /**
     * 菜单名称
     */
    @TableField("code")
    @Schema(description = "菜单编码")
    @NotNull
    private String code;

    /**
     * 菜单名称
     */
    @TableField("name")
    @Schema(description = "菜单名称")
    @NotNull
    private String name;

    /**
     * 菜单类型
     */
    @TableField("type")
    @Schema(description = "菜单类型")
    @NotNull
    private MenuType type;

    /**
     * 菜单序号
     */
    @TableField("sort")
    @Schema(description = "菜单序号")
    private Integer sort;

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
    @TableField("route")
    @Schema(description = "菜单路径")
    private String route;

    /**
     * icon
     */
    @TableField("icon")
    @Schema(description = "icon")
    private String icon;
}
