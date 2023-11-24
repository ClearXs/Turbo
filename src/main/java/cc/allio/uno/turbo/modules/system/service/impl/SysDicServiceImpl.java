package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysDic;
import cc.allio.uno.turbo.modules.system.mapper.SysDicMapper;
import cc.allio.uno.turbo.modules.system.service.ISysDicService;
import org.springframework.stereotype.Service;

@Service
public class SysDicServiceImpl extends TurboTreeCrudServiceImpl<SysDicMapper, SysDic> implements ISysDicService {
}
