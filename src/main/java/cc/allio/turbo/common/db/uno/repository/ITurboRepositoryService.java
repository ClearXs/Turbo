package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.exception.Exceptions;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 桥接{@link com.baomidou.mybatisplus.extension.service.IService}的功能
 *
 * @author jiangwei
 * @date 2024/2/3 00:18
 * @since 0.1.0
 */
public interface ITurboRepositoryService<T extends Entity, ID extends Serializable> extends IService<T> {

    @Override
    default boolean save(T entity) {
        return getRepository().save(entity) != null;
    }

    @Override
    default boolean saveBatch(Collection<T> entityList, int batchSize) {
        return getRepository().saveAll(entityList) != null;
    }

    @Override
   default boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return getRepository().save
    }

    @Override
    default boolean removeById(Serializable id) {
        return IService.super.removeById(id);
    }

    @Override
    default boolean removeById(Serializable id, boolean useFill) {
        return IService.super.removeById(id, useFill);
    }

    @Override
    default boolean removeById(T entity) {
        return IService.super.removeById(entity);
    }

    @Override
    default boolean removeByMap(Map<String, Object> columnMap) {
        return IService.super.removeByMap(columnMap);
    }

    @Override
    default boolean remove(Wrapper<T> queryWrapper) {
        return IService.super.remove(queryWrapper);
    }

    @Override
    default boolean removeByIds(Collection<?> list) {
        return IService.super.removeByIds(list);
    }

    @Override
    default boolean removeByIds(Collection<?> list, boolean useFill) {
        return IService.super.removeByIds(list, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list) {
        return IService.super.removeBatchByIds(list);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return IService.super.removeBatchByIds(list, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return IService.super.removeBatchByIds(list, batchSize);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return IService.super.removeBatchByIds(list, batchSize, useFill);
    }

    @Override
    default boolean updateById(T entity) {
        return IService.super.updateById(entity);
    }

    @Override
    default boolean update(Wrapper<T> updateWrapper) {
        return IService.super.update(updateWrapper);
    }

    @Override
    default boolean update(T entity, Wrapper<T> updateWrapper) {
        return IService.super.update(entity, updateWrapper);
    }

    @Override
    default boolean updateBatchById(Collection<T> entityList) {
        return IService.super.updateBatchById(entityList);
    }

    @Override
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {

    }

    @Override
    default boolean saveOrUpdate(T entity) {

    }

    @Override
    default T getById(Serializable id) {
        return IService.super.getById(id);
    }

    @Override
    default Optional<T> getOptById(Serializable id) {
        return IService.super.getOptById(id);
    }

    @Override
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        return IService.super.listByIds(idList);
    }

    @Override
    default List<T> listByMap(Map<String, Object> columnMap) {
        return IService.super.listByMap(columnMap);
    }

    @Override
    default T getOne(Wrapper<T> queryWrapper) {
        return IService.super.getOne(queryWrapper);
    }

    @Override
    default Optional<T> getOneOpt(Wrapper<T> queryWrapper) {
        return IService.super.getOneOpt(queryWrapper);
    }

    @Override
    default T getOne(Wrapper<T> queryWrapper, boolean throwEx) {

    }

    @Override
    default Optional<T> getOneOpt(Wrapper<T> queryWrapper, boolean throwEx) {

    }

    @Override
    default Map<String, Object> getMap(Wrapper<T> queryWrapper) {

    }

    @Override
    default <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {

    }

    @Override
    default boolean exists(Wrapper<T> queryWrapper) {
        return IService.super.exists(queryWrapper);
    }

    @Override
    default long count() {
        return IService.super.count();
    }

    @Override
    default long count(Wrapper<T> queryWrapper) {
        return IService.super.count(queryWrapper);
    }

    @Override
    default List<T> list(Wrapper<T> queryWrapper) {
        return IService.super.list(queryWrapper);
    }

    @Override
    default List<T> list(IPage<T> page, Wrapper<T> queryWrapper) {
        return IService.super.list(page, queryWrapper);
    }

    @Override
    default List<T> list() {
        return IService.super.list();
    }

    @Override
    default List<T> list(IPage<T> page) {
        return IService.super.list(page);
    }

    @Override
    default <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        return IService.super.page(page, queryWrapper);
    }

    @Override
    default <E extends IPage<T>> E page(E page) {
        return IService.super.page(page);
    }

    @Override
    default List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return IService.super.listMaps(queryWrapper);
    }

    @Override
    default List<Map<String, Object>> listMaps(IPage<? extends Map<String, Object>> page, Wrapper<T> queryWrapper) {
        return IService.super.listMaps(page, queryWrapper);
    }

    @Override
    default List<Map<String, Object>> listMaps() {
        return IService.super.listMaps();
    }

    @Override
    default List<Map<String, Object>> listMaps(IPage<? extends Map<String, Object>> page) {
        return IService.super.listMaps(page);
    }

    @Override
    default <E> List<E> listObjs() {
        return IService.super.listObjs();
    }

    @Override
    default <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return IService.super.listObjs(mapper);
    }

    @Override
    default <E> List<E> listObjs(Wrapper<T> queryWrapper) {
        return IService.super.listObjs(queryWrapper);
    }

    @Override
    default <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return IService.super.listObjs(queryWrapper, mapper);
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper) {
        return IService.super.pageMaps(page, queryWrapper);
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return IService.super.pageMaps(page);
    }

    @Override
    default BaseMapper<T> getBaseMapper() {
        throw Exceptions.unoperate("getBaseMapper");
    }

    @Override
    default Class<T> getEntityClass() {
        return getRepository().getEntityType();
    }

    /**
     * 获取{@link ITurboCrudRepository}的实例
     *
     * @return {@link ITurboCrudRepository}
     */
    ITurboCrudRepository<T, ID> getRepository();
}
