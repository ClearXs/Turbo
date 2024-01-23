package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.turbo.modules.system.mapper.SysCategoryMapper;
import cc.allio.turbo.modules.system.service.ISysCategoryService;
import org.springframework.stereotype.Service;

@Service
public class SysCategoryServiceImpl extends TurboTreeCrudServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {
}
