package cc.allio.uno.turbo.system.mapper;

import cc.allio.uno.turbo.system.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户id查询该用户的角色
     *
     * @return SysRole for list
     */
    List<SysRole> findSysRoleByUserId(Long userId);
}
