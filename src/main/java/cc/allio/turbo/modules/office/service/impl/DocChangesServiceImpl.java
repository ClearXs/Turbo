package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocChanges;
import cc.allio.turbo.modules.office.mapper.DocChangesMapper;
import cc.allio.turbo.modules.office.service.IDocChangesService;
import org.springframework.stereotype.Service;

@Service
public class DocChangesServiceImpl extends TurboCrudServiceImpl<DocChangesMapper, DocChanges> implements IDocChangesService {
}
