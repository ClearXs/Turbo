package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysPost;
import cc.allio.uno.turbo.modules.system.mapper.SysPostMapper;
import cc.allio.uno.turbo.modules.system.service.ISysPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPostServiceImpl extends TurboCrudServiceImpl<SysPostMapper, SysPost> implements ISysPostService {
    @Override
    public List<SysPost> findPostByUserId(Long userId) {
        return getBaseMapper().findSysPostByUserId(userId);
    }
}
