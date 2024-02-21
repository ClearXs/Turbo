package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.util.BeanUtils;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * domain 相关操作
 *
 * @author jiangwei
 * @date 2024/1/18 21:47
 * @since 0.1.0
 */
public abstract class Domains {

    /**
     * @see #toEntity(Entity, Class, BiFunction)
     */
    public static <T extends Entity, D extends Entity> D toDomain(T entity, Class<D> domainClazz) {
        return toDomain(entity, domainClazz, null);
    }

    /**
     * 实体转换为领域对象
     *
     * @param entity      实体对象
     * @param domainClazz 领域类型
     * @param then        二次增强
     * @param <T>         实体类型
     * @param <D>         领域类型
     * @return entity
     */
    public static <T extends Entity, D extends Entity> D toDomain(T entity, Class<D> domainClazz, BiFunction<T, D, D> then) {
        D domain = BeanUtils.copy(entity, domainClazz);
        return Optional.ofNullable(then)
                .map(t -> t.apply(entity, domain))
                .orElse(domain);
    }

    /**
     * @see #toEntity(Entity, Class, BiFunction)
     */
    public static <T extends Entity, D extends Entity> T toEntity(D domain, Class<T> entityClazz) {
        return toEntity(domain, entityClazz, null);
    }

    /**
     * 领域对象转换为实体
     *
     * @param domain      领域对象
     * @param entityClazz 实体类型
     * @param then        二次增强
     * @param <T>         实体类型
     * @param <D>         领域类型
     * @return entity
     */
    public static <T extends Entity, D extends Entity> T toEntity(D domain, Class<T> entityClazz, BiFunction<D, T, T> then) {
        T entity = BeanUtils.copy(domain, entityClazz);
        return Optional.ofNullable(then)
                .map(t -> t.apply(domain, entity))
                .orElse(entity);
    }
}
