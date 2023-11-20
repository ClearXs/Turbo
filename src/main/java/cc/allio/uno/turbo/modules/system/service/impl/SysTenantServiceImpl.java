package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.modules.system.entity.SysTenant;
import cc.allio.uno.turbo.modules.system.mapper.SysTenantMapper;
import cc.allio.uno.turbo.modules.system.service.ISysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
}
