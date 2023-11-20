package cc.allio.uno.turbo.modules.system.mapper;

import cc.allio.uno.turbo.modules.system.entity.SysRole;
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

    /**
     * 获取角色对应的菜单id
     *
     * @param roleIds roleIds
     * @return
     */
    List<Long> findRoleMenuIdByIds(List<Long> roleIds);

    /**
     * 获取角色对应的菜单id
     *
     * @param codes
     * @return
     */
    List<Long> findRoleMenuIdByCodes(List<String> codes);
}
