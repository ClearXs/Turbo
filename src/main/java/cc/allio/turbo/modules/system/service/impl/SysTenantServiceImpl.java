package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysTenant;
import cc.allio.turbo.modules.system.mapper.SysTenantMapper;
import cc.allio.turbo.modules.system.service.ISysTenantService;
import org.springframework.stereotype.Service;

@Service
public class SysTenantServiceImpl extends TurboCrudServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
}
