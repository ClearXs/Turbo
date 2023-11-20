package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.mybatis.service.ITurboService;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.param.SysMenuParam;

import java.util.List;

public interface ISysMenuService extends ITurboService<SysMenu> {

    /**
     * 菜单树查询
     *
     * @param menuParam menuParam
     * @return
     */
    List<SysMenu> tree(SysMenuParam menuParam);

    /**
     * 删除菜单，需要判断是否包含下级的菜单
     *
     * @param ids
     * @return
     */
    boolean deleteMenu(List<Long> ids) throws BizException;
}
