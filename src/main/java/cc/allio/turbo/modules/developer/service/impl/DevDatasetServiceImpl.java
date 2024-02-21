package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.developer.entity.DevDataset;
import cc.allio.turbo.modules.developer.mapper.DevDatasetMapper;
import cc.allio.turbo.modules.developer.service.IDevDatasetService;
import org.springframework.stereotype.Service;

@Service
public class DevDatasetServiceImpl extends TurboCrudServiceImpl<DevDatasetMapper, DevDataset> implements IDevDatasetService {

}
