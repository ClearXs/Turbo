package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocHistory;
import cc.allio.turbo.modules.office.mapper.DocHistoryMapper;
import cc.allio.turbo.modules.office.service.IDocHistoryService;
import org.springframework.stereotype.Service;

@Service
public class DocHistoryServiceImpl extends TurboCrudServiceImpl<DocHistoryMapper, DocHistory> implements IDocHistoryService {
}
