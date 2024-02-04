package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.event.Subscriber;
import cc.allio.uno.core.util.ReflectTool;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.ListCrudRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 定义Repository Crud操作集合。参考至{@link org.springframework.data.repository.ListCrudRepository}
 *
 * @author jiangwei
 * @date 2024/1/23 23:47
 * @since 0.1.0
 */
public interface ITurboCrudRepository<T extends Entity, ID extends Serializable> extends ITurboRepository, ListCrudRepository<T, ID>, Subscriber<T> {

    @NotNull
    @Override
    default <S extends T> S save(@NotNull S entity) {
        getExecutor().insertPojo(entity);
        return entity;
    }

    @NotNull
    default <S extends T> S saveOrUpdate(@NotNull S entity) {
        getExecutor().saveOrUpdate(entity);
        return entity;
    }

    @NotNull
    @Override
    default <S extends T> List<S> saveAll(@NotNull Iterable<S> entities) {
        List<S> pojos = Lists.newArrayList(entities);
        getExecutor().batchInsertPojos(pojos);
        return pojos;
    }

    @NotNull
    @Override
    default Optional<T> findById(@NotNull ID id) {
        return Optional.ofNullable(getExecutor().queryOneById(getEntityType(), id));
    }

    @Override
    default boolean existsById(@NotNull ID id) {
        return findById(id).isPresent();
    }

    @NotNull
    @Override
    default List<T> findAll() {
        return getExecutor().queryList(getEntityType());
    }

    @NotNull
    @Override
    default List<T> findAllById(@NotNull Iterable<ID> ids) {
        return getExecutor().queryListByIds(getEntityType(), ids);
    }

    @Override
    default long count() {
        return findAll().size();
    }

    @Override
    default void deleteById(@NotNull ID id) {
        getExecutor().deleteById(getEntityType(), id);
    }

    @Override
    default void delete(@NotNull T entity) {
        getExecutor().delete(entity);
    }

    @Override
    default void deleteAllById(@NotNull Iterable<? extends ID> ids) {
        getExecutor().deleteAllById(getEntityType(), ids);
    }

    @Override
    default void deleteAll(@NotNull Iterable<? extends T> entities) {
        getExecutor().deleteAll(getEntityType(), entities);
    }

    @Override
    default void deleteAll() {
        getExecutor().deleteAll(getEntityType());
    }

    /**
     * 获取实体类型
     *
     * @return t class
     */
    default Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, ITurboCrudRepository.class, 0);
    }
}
