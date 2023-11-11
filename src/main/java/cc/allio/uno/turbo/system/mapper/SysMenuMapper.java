package cc.allio.uno.turbo.system.mapper;

import cc.allio.uno.turbo.system.entity.SysMenu;
import cc.allio.uno.turbo.system.param.SysMenuParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> tree(@Param("param") SysMenuParam param);
}
