package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;

/**
 * 由{@link ITurboRepository}继承，获取实体类型
 *
 * @author jiangwei
 * @date 2024/2/29 23:26
 * @since 0.1.1
 */
public interface EntityTypeAware<T extends Entity> {

    /**
     * set entity type
     *
     * @param entityType entityType
     */
    default void setEntityType(Class<T> entityType) {
        // subset
    }
}
