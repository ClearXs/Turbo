package cc.allio.turbo.modules.system.mapper;

import cc.allio.turbo.common.mybatis.mapper.TreeMapper;
import cc.allio.turbo.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysMenuMapper extends TreeMapper<SysMenu> {

}
