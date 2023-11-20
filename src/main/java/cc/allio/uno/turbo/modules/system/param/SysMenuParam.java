package cc.allio.uno.turbo.modules.system.param;

import lombok.Data;

import java.util.List;

@Data
public class SysMenuParam {

    /**
     * id
     */
    private Long id;

    /**
     * 菜单id
     */
    private List<Long> menuIds;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 是否排除本级
     */
    private Boolean exclude;
}
