package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocTrash;
import cc.allio.turbo.modules.office.mapper.DocTrashMapper;
import cc.allio.turbo.modules.office.service.IDocTrashService;
import org.springframework.stereotype.Service;

@Service
public class DocTrashServiceImpl extends TurboCrudServiceImpl<DocTrashMapper, DocTrash> implements IDocTrashService {
}
