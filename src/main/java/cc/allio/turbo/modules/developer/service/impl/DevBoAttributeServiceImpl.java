package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboTreeCrudServiceImpl;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.turbo.modules.developer.domain.DevAttributeProps;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import cc.allio.turbo.modules.developer.entity.DomainEntity;
import cc.allio.turbo.modules.developer.mapper.DevBoAttributeMapper;
import cc.allio.turbo.modules.developer.service.IDevBoAttributeService;
import cc.allio.uno.data.orm.dsl.DSLName;
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
                        .setCode(DomainEntity.ID)
                        .setName("主键")
                        .setField(DomainEntity.ID)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPk(true)
                                        .setNonNull(true)
                                        .setPrecision(64)
                                        .setUnique(true)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(id);

        // 创建人
        DevBoAttribute createBy =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.CREATED_BY))
                        .setName("创建人")
                        .setField(DomainEntity.CREATED_BY)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPrecision(64)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(createBy);

        // 创建时间
        DevBoAttribute createTime =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.CREATED_TIME))
                        .setName("创建时间")
                        .setField(DomainEntity.CREATED_TIME)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.TIMESTAMP)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(createTime);

        // 更新人
        DevBoAttribute updateBy =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.UPDATED_BY))
                        .setName("更新人")
                        .setField(DomainEntity.UPDATED_BY)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.BIGINT)
                                        .setPrecision(64)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(updateBy);

        // 更新时间
        DevBoAttribute updateTime =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.UPDATED_TIME))
                        .setName("更新时间")
                        .setField(DomainEntity.UPDATED_TIME)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.TIMESTAMP)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(updateTime);

        // 逻辑删除
        DevBoAttribute isDeleted =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.IS_DELETED))
                        .setName("逻辑删除")
                        .setField(DomainEntity.IS_DELETED)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.SMALLINT)
                                        .setDefaulted(true)
                                        .toJson()
                        )
                        .setField("is_deleted");
        defaultBoAttributes.add(isDeleted);

        // 版本号
        DevBoAttribute version =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.VERSION))
                        .setName("版本号")
                        .setField(DomainEntity.VERSION)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.INTEGER)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(version);

        // 租户
        DevBoAttribute tenantId =
                new DevBoAttribute()
                        .setCode(DSLName.toHump(DomainEntity.TENANT_ID))
                        .setName("租户")
                        .setField(DomainEntity.TENANT_ID)
                        .setAttrType(AttributeType.FIELD)
                        .setProps(
                                new DevAttributeProps()
                                        .setType(FieldType.VARCHAR)
                                        .setPrecision(32)
                                        .setDefaulted(true)
                                        .toJson()
                        );
        defaultBoAttributes.add(tenantId);

        return defaultBoAttributes;
    }
}
