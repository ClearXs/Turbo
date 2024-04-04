package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.db.uno.repository.mybatis.WrapperAdapter;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * uno-data 桥接{@link com.baomidou.mybatisplus.extension.service.IService}的功能
 *
 * @author j.x
 * @date 2024/2/3 00:18
 * @since 0.1.0
 */
public interface ITurboCrudRepositoryService<T extends Entity> extends ITurboCrudService<T>, ITurboRepository<T> {

    /**
     * 获取{@link ITurboCrudRepository}的实例
     *
     * @return {@link ITurboCrudRepository}
     */
    default ITurboCrudRepository<T> getRepository() {
      return null;
    }

    // ============================== default method ==============================

    @Override
    default boolean save(T entity) {
        return getRepository().save(entity) != null;
    }

    @Override
    default boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    default boolean saveOrUpdateBatch(Collection<T> entityList) {
        return saveOrUpdateBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    default boolean saveBatch(Collection<T> entityList, int batchSize) {
        return getRepository().saveAll(entityList) != null;
    }

    @Override
    default boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return entityList.stream().allMatch(this::saveOrUpdate);
    }

    @Override
    default boolean removeById(Serializable id) {
        return removeById(id, false);
    }

    @Override
    default boolean removeById(Serializable id, boolean useFill) {
        getRepository().deleteById(id);
        return true;
    }

    @Override
    default boolean removeById(T entity) {
        getRepository().delete(entity);
        return true;
    }

    @Override
    default boolean removeByMap(Map<String, Object> columnMap) {
        return getRepository().getExecutor()
                .delete(f -> {
                    for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
                        f.eq(entry.getKey(), entry.getValue());
                    }
                    return f;
                });
    }

    @Override
    default boolean remove(Wrapper<T> queryWrapper) {
        return getRepository().getExecutor()
                .delete(f -> WrapperAdapter.adapt(queryWrapper, f));
    }

    @Override
    default boolean removeByIds(Collection<?> list) {
        return removeByIds(list, false);
    }

    @Override
    default boolean removeByIds(Collection<?> list, boolean useFill) {
        return removeBatchByIds(list, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list) {
        return removeBatchByIds(list, false);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return removeBatchByIds(list, DEFAULT_BATCH_SIZE, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return removeBatchByIds(list, batchSize, false);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return getRepository()
                .getExecutor()
                .deleteAllById(getEntityClass(), list.stream().map(Serializable.class::cast).toList());
    }

    @Override
    default boolean updateById(T entity) {
        return getRepository()
                .getExecutor()
                .updatePojo(entity);
    }

    @Override
    default boolean update(Wrapper<T> updateWrapper) {
        return getRepository()
                .getExecutor()
                .update(f -> WrapperAdapter.adapt(updateWrapper, f));
    }

    @Override
    default boolean update(T entity, Wrapper<T> updateWrapper) {
        return getRepository()
                .getExecutor()
                .updatePojoByCondition(entity, f -> WrapperAdapter.adapt(updateWrapper, f));
    }

    @Override
    default boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return entityList.stream().allMatch(this::updateById);
    }

    @Override
    default boolean saveOrUpdate(T entity) {
        return getRepository().saveOrUpdate(entity) != null;
    }

    @Override
    default <V extends T> V details(Serializable id) {
        return (V) getById(id);
    }

    @Override
    default T getById(Serializable id) {
        return getRepository().findById(id).orElse(null);
    }

    @Override
    default Optional<T> getOptById(Serializable id) {
        return getRepository().findById(id);
    }

    @Override
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        return getRepository()
                .getExecutor()
                .queryListByIds(getEntityClass(), idList);
    }

    @Override
    default List<T> listByMap(Map<String, Object> columnMap) {
        Set<String> columns = columnMap.keySet();
        return getRepository()
                .getExecutor()
                .queryList(getEntityClass(), f -> f.select(columns));
    }

    @Override
    default T getOne(Wrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    @Override
    default Optional<T> getOneOpt(Wrapper<T> queryWrapper) {
        return getOneOpt(queryWrapper, false);
    }

    @Override
    default T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        try {
            return getRepository()
                    .getExecutor()
                    .queryOne(getEntityClass(), f -> WrapperAdapter.adapt(queryWrapper, f));
        } catch (Throwable ex) {
            if (throwEx) {
                throw new MybatisPlusException(ex);
            }
        }
        return null;
    }

    @Override
    default Optional<T> getOneOpt(Wrapper<T> queryWrapper, boolean throwEx) {
        T entity = getOne(queryWrapper, throwEx);
        return Optional.ofNullable(entity);
    }

    @Override
    default Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return getRepository()
                .getExecutor()
                .queryMap(f -> WrapperAdapter.adapt(queryWrapper, f).from(getEntityClass()));
    }

    @Override
    default <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return getOneOpt(queryWrapper)
                .map(mapper)
                .orElse(null);
    }

    @Override
    default boolean exists(Wrapper<T> queryWrapper) {
        return count(queryWrapper) > 0;
    }

    @Override
    default long count() {
        return getRepository().count();
    }

    @Override
    default long count(Wrapper<T> queryWrapper) {
        return getRepository()
                .getExecutor()
                .queryListMap(f -> WrapperAdapter.adapt(queryWrapper, f).from(getEntityClass())).size();
    }

    @Override
    default List<T> list(Wrapper<T> queryWrapper) {
        return list(null, queryWrapper);
    }

    @Override
    default List<T> list(IPage<T> page, Wrapper<T> queryWrapper) {
        return getRepository()
                .getExecutor()
                .queryList(getEntityClass(), f -> WrapperAdapter.adapt(queryWrapper, f).from(getEntityClass()));
    }

    @Override
    default List<T> list() {
        return getRepository().findAll();
    }

    @Override
    default List<T> list(IPage<T> page) {
        return getRepository().findAll();
    }

    @Override
    default <E extends IPage<T>> E page(E page) {
        return page(page, Wrappers.emptyWrapper());
    }

    @Override
    default <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        cc.allio.uno.data.orm.executor.IPage<T> unoPage = createUnoPage(page);
        cc.allio.uno.data.orm.executor.IPage<T> resultPage =
                getRepository()
                        .getExecutor()
                        .queryPage(unoPage, f -> WrapperAdapter.adapt(queryWrapper, f), getEntityClass());
        return (E) createMybatisPage(resultPage);
    }

    @Override
    default List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return listMaps(null, queryWrapper);
    }

    @Override
    default List<Map<String, Object>> listMaps(IPage<? extends Map<String, Object>> page, Wrapper<T> queryWrapper) {
        return getRepository()
                .getExecutor()
                .queryListMap(f -> WrapperAdapter.adapt(queryWrapper, f).from(getEntityClass()));
    }

    @Override
    default List<Map<String, Object>> listMaps() {
        return getRepository()
                .getExecutor()
                .queryListMap(f -> f);
    }

    @Override
    default List<Map<String, Object>> listMaps(IPage<? extends Map<String, Object>> page) {
        return getRepository()
                .getExecutor()
                .queryListMap(f -> f.from(getEntityClass()));
    }

    @Override
    default <E> List<E> listObjs() {
        return (List<E>) getRepository()
                .getExecutor()
                .queryList(getEntityClass());
    }

    @Override
    default <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return listObjs(Wrappers.emptyWrapper(), mapper);
    }

    @Override
    default <E> List<E> listObjs(Wrapper<T> queryWrapper) {
        return (List<E>) getRepository()
                .getExecutor()
                .queryList(getEntityClass(), f -> WrapperAdapter.adapt(queryWrapper, f));
    }

    @Override
    default <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return getRepository()
                .getExecutor()
                .queryList(getEntityClass(), f -> WrapperAdapter.adapt(queryWrapper, f))
                .stream()
                .map(mapper)
                .toList();
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return pageMaps(page, Wrappers.emptyWrapper());
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper) {
        cc.allio.uno.data.orm.executor.IPage<Map<String, Object>> unoPage = createUnoPage(page);
        cc.allio.uno.data.orm.executor.IPage<Map<String, Object>> resultPage =
                getRepository()
                        .getExecutor()
                        .queryPageMap(unoPage, f -> WrapperAdapter.adapt(queryWrapper, f).from(getEntityClass()));
        return (E) createMybatisPage(resultPage);
    }

    @Override
    default BaseMapper<T> getBaseMapper() {
        throw Exceptions.unOperate("getBaseMapper");
    }

    @Override
    default Class<T> getEntityClass() {
        return getRepository().getEntityType();
    }

    default <T> cc.allio.uno.data.orm.executor.IPage<T> createUnoPage(IPage<T> page) {
        cc.allio.uno.data.orm.executor.IPage<T> unoPage = cc.allio.uno.data.orm.executor.IPage.create(page.getCurrent(), page.getSize(), page.getTotal(), page.searchCount());
        unoPage.setRecords(page.getRecords());
        return unoPage;
    }

    default <T> IPage<T> createMybatisPage(cc.allio.uno.data.orm.executor.IPage<T> page) {
        Page<T> mybatisPage = new Page<>();
        mybatisPage.setCurrent(page.getCurrent());
        mybatisPage.setSize(page.getSize());
        mybatisPage.setTotal(page.getTotal());
        mybatisPage.setSearchCount(page.isSearchCount());
        mybatisPage.setRecords(page.getRecords());
        return mybatisPage;
    }

    // ============================== static method ==============================

    /**
     * 根据传递的实体类型，快速获取{@link ITurboCrudRepositoryService}实例，其根据{@link CommandExecutorFactory#getDSLExecutor()}来构建执行器部分
     *
     * @param entityClass entityClass
     * @param <T>         实体类型
     * @return SimpleTurboCrudRepositoryServiceImpl
     * @see cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl
     */
    static <T extends Entity> ITurboCrudRepositoryService<T> simply(Class<T> entityClass) {
        return new SimpleTurboCrudRepositoryServiceImpl<>(entityClass);
    }

    /**
     * 根据传递的实体类型，快速获取{@link ITurboCrudRepositoryService}实例
     *
     * @param commandExecutor commandExecutor
     * @param entityClass     entityClass
     * @param <T>             实体类型
     * @return SimpleTurboCrudRepositoryServiceImpl
     * @see cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl
     */
    static <T extends Entity> ITurboCrudRepositoryService<T> simply(AggregateCommandExecutor commandExecutor, Class<T> entityClass) {
        return new SimpleTurboCrudRepositoryServiceImpl<>(commandExecutor, entityClass);
    }
}
