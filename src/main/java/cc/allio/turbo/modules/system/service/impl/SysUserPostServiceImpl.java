package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.system.entity.SysUserPost;
import cc.allio.turbo.modules.system.mapper.SysUserPostMapper;
import cc.allio.turbo.modules.system.service.ISysUserPostService;
import org.springframework.stereotype.Service;

@Service
public class SysUserPostServiceImpl extends TurboCrudServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {
}
