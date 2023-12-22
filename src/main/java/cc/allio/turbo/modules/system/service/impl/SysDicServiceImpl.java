package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.system.service.ISysDicService;
import cc.allio.turbo.modules.system.entity.SysDic;
import cc.allio.turbo.modules.system.mapper.SysDicMapper;
import org.springframework.stereotype.Service;

@Service
public class SysDicServiceImpl extends TurboTreeCrudServiceImpl<SysDicMapper, SysDic> implements ISysDicService {
}
