package cc.allio.uno.turbo.system.vo;

import cc.allio.uno.turbo.common.support.DomainTree;
import cc.allio.uno.turbo.system.constant.MenuType;
import cc.allio.uno.turbo.system.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysMenuTreeVO extends DomainTree<SysMenu> {

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

    public SysMenuTreeVO(SysMenu expand) {
        super(expand);
    }
}
