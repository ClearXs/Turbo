package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocHistoryUser;
import cc.allio.turbo.modules.office.mapper.DocHistoryUserMapper;
import cc.allio.turbo.modules.office.service.IDocHistoryUserService;
import org.springframework.stereotype.Service;

@Service
public class DocHistoryUserServiceImpl extends TurboCrudServiceImpl<DocHistoryUserMapper, DocHistoryUser> implements IDocHistoryUserService {
}
