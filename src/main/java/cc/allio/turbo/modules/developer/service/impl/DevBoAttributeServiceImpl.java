package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.turbo.modules.developer.domain.DevAttributeProps;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import cc.allio.turbo.modules.developer.mapper.DevBoAttributeMapper;
import cc.allio.turbo.modules.developer.service.IDevBoAttributeService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevBoAttributeServiceImpl extends TurboTreeCrudServiceImpl<DevBoAttributeMapper, DevBoAttribute> implements IDevBoAttributeService {

    @Override
    public boolean save(DevBoAttribute entity) {
        boolean saved = super.save(entity);
        if (AttributeType.TABLE == entity.getAttrType()) {
            List<DevBoAttribute> defaultAttributes = getDefaultAttributes();
            for (DevBoAttribute defaultAttribute : defaultAttributes) {
                defaultAttribute.setBoId(entity.getBoId());
                defaultAttribute.setParentId(entity.getId());
            }
            return saved && saveBatch(defaultAttributes);
        }
        return saved;
    }

    @Override
    public List<DevBoAttribute> getDefaultAttributes() {
        List<DevBoAttribute> defaultBoAttributes = Lists.newArrayList();
        // 主键
        DevBoAttribute id =
                new DevBoAttribute()
                        .setCode("id")
                        .setField("id")
                        .setName("主键")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPk(true)
                                        .setNonNull(true)
                                        .setPrecision(64)
                                        .setUnique(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(id);

        // 创建人
        DevBoAttribute createBy =
                new DevBoAttribute()
                        .setCode("createBy")
                        .setName("创建人")
                        .setField("create_by")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPrecision(64)
                                        .toJson()
                        );
        defaultBoAttributes.add(createBy);

        // 创建时间
        DevBoAttribute createTime =
                new DevBoAttribute()
                        .setCode("createTime")
                        .setName("创建时间")
                        .setField("create_time")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.TIMESTAMP)
                                        .toJson()
                        );
        defaultBoAttributes.add(createTime);

        // 更新时间
        DevBoAttribute updateTime =
                new DevBoAttribute()
                        .setCode("updateTime")
                        .setName("更新时间")
                        .setField("update_time")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.TIMESTAMP)
                                        .toJson()
                        );
        defaultBoAttributes.add(updateTime);

        // 更新时间
        DevBoAttribute updateBy =
                new DevBoAttribute()
                        .setCode("updateBy")
                        .setName("更新人")
                        .setField("update_by")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPrecision(64)
                                        .toJson()
                        );
        defaultBoAttributes.add(updateBy);

        // 逻辑删除
        DevBoAttribute isDeleted =
                new DevBoAttribute()
                        .setCode("isDeleted")
                        .setName("逻辑删除")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.SMALLINT)
                                        .toJson()
                        )
                        .setField("is_deleted");
        defaultBoAttributes.add(isDeleted);

        // 版本号
        DevBoAttribute version =
                new DevBoAttribute()
                        .setCode("version")
                        .setName("版本号")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.INTEGER)
                                        .toJson()
                        )
                        .setField("version");
        defaultBoAttributes.add(version);

        // 租户
        DevBoAttribute tenantId =
                new DevBoAttribute()
                        .setCode("tenantId")
                        .setName("租户")
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.VARCHAR)
                                        .toJson()
                        )
                        .setField("tenant_id");
        defaultBoAttributes.add(tenantId);

        return defaultBoAttributes;
    }
}
