package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocCooperator;
import cc.allio.turbo.modules.office.mapper.DocCooperatorMapper;
import cc.allio.turbo.modules.office.service.IDocCooperatorService;
import org.springframework.stereotype.Service;

@Service
public class DocCooperatorServiceImpl extends TurboCrudServiceImpl<DocCooperatorMapper, DocCooperator> implements IDocCooperatorService {
}
