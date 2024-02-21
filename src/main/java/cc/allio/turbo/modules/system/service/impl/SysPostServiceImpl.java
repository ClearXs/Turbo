package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysPost;
import cc.allio.turbo.modules.system.mapper.SysPostMapper;
import cc.allio.turbo.modules.system.service.ISysPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPostServiceImpl extends TurboCrudServiceImpl<SysPostMapper, SysPost> implements ISysPostService {
    @Override
    public List<SysPost> findPostByUserId(Long userId) {
        return getBaseMapper().findSysPostByUserId(userId);
    }
}
