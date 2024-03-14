package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.event.Subscriber;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.ListCrudRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 定义Repository Crud操作集合。参考至{@link org.springframework.data.repository.ListCrudRepository}
 *
 * @author j.x
 * @date 2024/1/23 23:47
 * @since 0.1.0
 */
public interface ITurboCrudRepository<T extends Entity> extends ITurboRepository<T>, ListCrudRepository<T, Serializable>, Subscriber<T> {

    @Override
    default <S extends T> S save(S entity) {
        getExecutor().insertPojo(entity);
        return entity;
    }

    @NotNull
    default <S extends T> S saveOrUpdate(S entity) {
        getExecutor().saveOrUpdate(entity);
        return entity;
    }

    @Override
    default <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> pojos = Lists.newArrayList(entities);
        getExecutor().batchInsertPojos(pojos);
        return pojos;
    }

    @Override
    default Optional<T> findById(Serializable id) {
        return Optional.ofNullable(getExecutor().queryOneById(getEntityType(), id));
    }

    @Override
    default boolean existsById(Serializable id) {
        return findById(id).isPresent();
    }

    @NotNull
    @Override
    default List<T> findAll() {
        return getExecutor().queryList(getEntityType());
    }

    @NotNull
    @Override
    default List<T> findAllById(Iterable<Serializable> ids) {
        return getExecutor().queryListByIds(getEntityType(), ids);
    }

    @Override
    default long count() {
        return findAll().size();
    }

    @Override
    default void deleteById(Serializable id) {
        getExecutor().deleteById(getEntityType(), id);
    }

    @Override
    default void delete(T entity) {
        getExecutor().delete(entity);
    }

    @Override
    default void deleteAllById(Iterable<? extends Serializable> ids) {
        getExecutor().deleteAllById(getEntityType(), ids);
    }

    @Override
    default void deleteAll(Iterable<? extends T> entities) {
        getExecutor().deleteAll(getEntityType(), entities);
    }

    @Override
    default void deleteAll() {
        getExecutor().deleteAll(getEntityType());
    }

}
