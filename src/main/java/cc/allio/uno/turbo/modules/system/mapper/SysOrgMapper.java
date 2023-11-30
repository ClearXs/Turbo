package cc.allio.uno.turbo.modules.system.mapper;

import cc.allio.uno.turbo.common.mybatis.mapper.TreeMapper;
import cc.allio.uno.turbo.modules.system.entity.SysOrg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOrgMapper extends TreeMapper<SysOrg> {
}
