package cc.allio.uno.turbo.modules.system.mapper;

import cc.allio.uno.turbo.modules.system.entity.SysPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * 根据用户id查询该用户的岗位
     *
     * @return SysRole for list
     */
    List<SysPost> findSysPostByUserId(Long userId);
}
