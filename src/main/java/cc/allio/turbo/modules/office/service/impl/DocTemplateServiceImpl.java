package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocTemplate;
import cc.allio.turbo.modules.office.mapper.DocTemplateMapper;
import cc.allio.turbo.modules.office.service.IDocTemplateService;
import org.springframework.stereotype.Service;

@Service
public class DocTemplateServiceImpl extends TurboCrudServiceImpl<DocTemplateMapper, DocTemplate> implements IDocTemplateService {
}
