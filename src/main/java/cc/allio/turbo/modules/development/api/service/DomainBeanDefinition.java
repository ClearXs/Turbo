package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.modules.development.api.DomainObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * 定义 Domain Bean Definition
 *
 * @author j.x
 * @date 2024/3/5 23:03
 * @since 0.1.1
 */
@Setter
@Getter
public class DomainBeanDefinition extends GenericBeanDefinition {

    private String domainName;
    private Class<? extends DomainObject> domainObjectClass;
    private Class<? extends IDomainService<? extends DomainObject>> domainServiceClass;

    public DomainBeanDefinition(BeanDefinition original) {
        super(original);
        setAbstract(false);
        setAutowireMode(AUTOWIRE_BY_TYPE);
        setBeanClass(domainServiceClass);
    }

    /**
     * 判断是否是接口
     */
    public boolean isInterface() {
        return domainServiceClass.isInterface();
    }
}
