package cc.allio.uno.turbo.modules.system.service;

import cc.allio.uno.turbo.common.mybatis.service.ITurboTreeCrudService;
import cc.allio.uno.turbo.modules.system.entity.SysOrg;

import java.util.List;

public interface ISysOrgService extends ITurboTreeCrudService<SysOrg> {

    /**
     * 获取某个用户的组织信息
     *
     * @param userId userId
     * @return
     */
    List<SysOrg> getOrgByUserId(Long userId);
}
