package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.domain.Domains;
import cc.allio.turbo.common.domain.JsonDomain;
import cc.allio.turbo.common.db.mybatis.service.ITurboTreeCrudService;
import cc.allio.turbo.modules.developer.domain.BoAttributeTree;
import cc.allio.turbo.modules.developer.domain.DevAttributeProps;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.StringUtils;

import java.util.List;

public interface IDevBoAttributeService extends ITurboTreeCrudService<DevBoAttribute> {

    /**
     * @return 属性项列表
     */
    List<DevBoAttribute> getDefaultAttributes();

    /**
     * tree domain to entity
     *
     * @param domain v
     * @return entity
     */
    default DevBoAttribute domainToEntity(BoAttributeTree domain) {
        return Domains.toEntity(
                domain,
                DevBoAttribute.class,
                (d, e) -> {
                    DevAttributeProps attributeProps = BeanUtils.copy(domain, DevAttributeProps.class);
                    if (attributeProps != null) {
                        e.setProps(attributeProps.toJson());
                    }
                    return e;
                });
    }

    /**
     * entity to domain
     *
     * @param entity entity
     * @return domain
     */
    default BoAttributeTree entityToDomain(DevBoAttribute entity) {
        return Domains.toDomain(
                entity,
                BoAttributeTree.class,
                (e, d) -> {
                    String props = e.getProps();
                    if (StringUtils.isBlank(props)) {
                        return d;
                    }
                    DevAttributeProps attributeProps = JsonDomain.from(props, DevAttributeProps.class);
                    BeanUtils.copy(attributeProps, d);
                    return d;
                });
    }
}
