package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.modules.system.entity.SysUserPost;
import cc.allio.uno.turbo.modules.system.mapper.SysUserPostMapper;
import cc.allio.uno.turbo.modules.system.service.ISysUserPostService;
import org.springframework.stereotype.Service;

@Service
public class SysUserPostServiceImpl extends TurboCrudServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {
}
