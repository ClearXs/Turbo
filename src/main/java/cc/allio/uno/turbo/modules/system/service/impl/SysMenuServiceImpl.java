package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.mybatis.service.impl.TurboServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.mapper.SysMenuMapper;
import cc.allio.uno.turbo.modules.system.service.ISysMenuService;
import cc.allio.uno.turbo.modules.system.param.SysMenuParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends TurboServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> tree(SysMenuParam menuParam) {
        return getBaseMapper().tree(menuParam);
    }

    @Override
    public boolean deleteMenu(List<Long> ids) throws BizException {
        // TODO 需要重构代码
        for (Long id : ids) {
            SysMenuParam menuParam = new SysMenuParam();
            menuParam.setExclude(true);
            menuParam.setId(id);
            List<SysMenu> children = getBaseMapper().tree(menuParam);
            if (!children.isEmpty()) {
                throw new BizException(ExceptionCodes.MENU_DELETE_FAILED);
            }
        }
        return removeBatchByIds(ids);
    }
}
