package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.turbo.modules.developer.mapper.DevDataSourceMapper;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import org.springframework.stereotype.Service;

@Service
public class DevDataSourceServiceImpl extends TurboCrudServiceImpl<DevDataSourceMapper, DevDataSource> implements IDevDataSourceService {
}
