package cc.allio.turbo.modules.system.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.system.entity.SysPost;

import java.util.List;

public interface ISysPostService extends ITurboCrudService<SysPost> {

    /**
     * 根据用户id获取用户岗位信息
     *
     * @param userId userId
     */
    List<SysPost> findPostByUserId(Long userId);
}
