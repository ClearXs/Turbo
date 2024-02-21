package cc.allio.turbo.modules.system.mapper;

import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import cc.allio.turbo.modules.system.entity.SysOrg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOrgMapper extends TreeMapper<SysOrg> {
}
