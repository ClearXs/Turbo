package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysOrg;
import cc.allio.uno.turbo.modules.system.entity.SysUserOrg;
import cc.allio.uno.turbo.modules.system.mapper.SysOrgMapper;
import cc.allio.uno.turbo.modules.system.service.ISysOrgService;
import cc.allio.uno.turbo.modules.system.service.ISysUserOrgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SysOrgServiceImpl extends TurboTreeCrudServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

    private final ISysUserOrgService userOrgService;

    @Override
    public List<SysOrg> getOrgByUserId(Long userId) {
        List<SysUserOrg> sysUserOrgs = userOrgService.list(Wrappers.<SysUserOrg>lambdaQuery().eq(SysUserOrg::getUserId, userId));
        if (CollectionUtils.isEmpty(sysUserOrgs)) {
            return Collections.emptyList();
        }
        List<Long> orgIds =
                sysUserOrgs.stream()
                        .map(SysUserOrg::getOrgId)
                        .toList();
        return listByIds(orgIds);
    }
}
