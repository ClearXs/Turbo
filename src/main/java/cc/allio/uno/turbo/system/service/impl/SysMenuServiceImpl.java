package cc.allio.uno.turbo.system.service.impl;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.mybatis.service.impl.TurboServiceImpl;
import cc.allio.uno.turbo.system.entity.SysMenu;
import cc.allio.uno.turbo.system.mapper.SysMenuMapper;
import cc.allio.uno.turbo.system.param.SysMenuParam;
import cc.allio.uno.turbo.system.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends TurboServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> tree(SysMenuParam menuParam) {
        return getBaseMapper().tree(menuParam);
    }

    @Override
    public boolean deleteMenu(long id) throws BizException {
        SysMenuParam menuParam = new SysMenuParam();
        menuParam.setExclude(true);
        menuParam.setId(id);
        List<SysMenu> children = getBaseMapper().tree(menuParam);
        if (!children.isEmpty()) {
            throw new BizException(ExceptionCodes.MENU_DELETE_FAILED);
        }
        return removeById(id);
    }
}
