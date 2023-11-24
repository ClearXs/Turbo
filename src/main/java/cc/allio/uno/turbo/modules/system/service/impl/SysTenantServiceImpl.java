package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysTenant;
import cc.allio.uno.turbo.modules.system.mapper.SysTenantMapper;
import cc.allio.uno.turbo.modules.system.service.ISysTenantService;
import org.springframework.stereotype.Service;

@Service
public class SysTenantServiceImpl extends TurboCrudServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
}
