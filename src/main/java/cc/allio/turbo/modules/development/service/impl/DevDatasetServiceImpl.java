package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.development.entity.DevDataset;
import cc.allio.turbo.modules.development.mapper.DevDatasetMapper;
import cc.allio.turbo.modules.development.service.IDevDatasetService;
import org.springframework.stereotype.Service;

@Service
public class DevDatasetServiceImpl extends TurboCrudServiceImpl<DevDatasetMapper, DevDataset> implements IDevDatasetService {

}
