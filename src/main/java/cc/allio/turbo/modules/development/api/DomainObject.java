package cc.allio.turbo.modules.development.api;

import cc.allio.turbo.modules.development.api.annotation.Domain;
import cc.allio.turbo.modules.development.entity.DomainEntity;
import cc.allio.uno.core.StringPool;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 领域对象定义，可以通过编码方式操作领域对象
 *
 * @author j.x
 * @date 2024/2/27 18:24
 * @since 0.1.1
 */
public abstract class DomainObject extends DomainEntity {

    /**
     * 获取该领域对象唯一标识。子类实现，默认读取该类上是否存在{@link cc.allio.turbo.modules.development.api.annotation.Domain}注解
     *
     * @return 领域对象唯一标识
     */
    public static <T extends DomainObject> String getKey(Class<T> domainObjectClass) {
        Domain domain = AnnotationUtils.findAnnotation(domainObjectClass, Domain.class);
        if (domain != null) {
            return domain.value();
        } else {
            return StringPool.EMPTY;
        }
    }
}
