package cc.allio.turbo.modules.message.service;

import cc.allio.turbo.common.cache.ICache;
import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;

public interface ISysMessageConfigService extends ITurboCrudRepositoryService<SysMessageConfig>, ICache {
}
