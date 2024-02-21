package cc.allio.turbo.modules.system.mapper;

import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import cc.allio.turbo.modules.system.entity.SysCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysCategoryMapper extends TreeMapper<SysCategory> {
}
