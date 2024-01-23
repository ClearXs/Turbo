package cc.allio.turbo.modules.developer.mapper;

import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DevBoAttributeMapper extends TreeMapper<DevBoAttribute> {
}
