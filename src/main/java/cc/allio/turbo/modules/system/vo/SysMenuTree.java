package cc.allio.turbo.modules.system.vo;

import cc.allio.turbo.common.support.DomainTree;
import cc.allio.turbo.modules.system.constant.MenuType;
import cc.allio.turbo.modules.system.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Setter
@Getter
public class SysMenuTree extends DomainTree<SysMenuTree, SysMenu> {

    public SysMenuTree(SysMenu sysMenu) {
        super(sysMenu, Comparator.comparingInt(SysMenuTree::getSort));
    }

    /**
     * 菜单域
     */
    @Schema(description = "scope")
    private String scope;

    /**
     * 菜单编码
     */
    @Schema(description = "菜单编码")
    private String code;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单类型
     */
    @Schema(description = "菜单类型")
    private MenuType type;

    /**
     * 菜单序号
     */
    @Schema(description = "菜单序号")
    private Integer sort;

    /**
     * 父级菜单
     */
    @Schema(description = "父级菜单")
    private Long parentId;

    /**
     * 别名
     */
    @Schema(description = "别名")
    private String alias;

    /**
     * 路径
     */
    @Schema(description = "菜单路径")
    private String route;

    /**
     * icon
     */
    @Schema(description = "icon")
    private String icon;

    /**
     * attrs
     */
    @Schema(description = "attrs")
    private String[] attrs;

}

