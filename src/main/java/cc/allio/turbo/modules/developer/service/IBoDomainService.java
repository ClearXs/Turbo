package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.event.Subscriber;
import cc.allio.turbo.common.db.uno.repository.LockRepositoryMethodInterceptor;
import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.api.DomainObject;
import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.api.service.IDomainService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * DomainService是业务对象的领域行为，所以其API都是关于业务对象的操作。所有API都包含一个BoId的参数。
 * <p>子类实现需要以下几点</p>
 * <ul>
 *     <li>维护Bo-Repository的映射关系</li>
 *     <li>基于{@link LockRepositoryMethodInterceptor}创建AOP对象</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2024/2/6 15:42
 * @since 0.1.0
 */
public interface IBoDomainService extends Subscriber<GeneralDomainObject> {

    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return true if success
     */
    default boolean save(Long boId, GeneralDomainObject entity) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.save(entity);
    }

    /**
     * @see #saveBatch(Long, Collection, int)
     */
    default boolean saveBatch(Long boId, Collection<GeneralDomainObject> entityList) throws BizException {
        return saveBatch(boId, entityList, -1);
    }

    /**
     * 批量保存
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     * @return true if success
     */
    default boolean saveBatch(Long boId, Collection<GeneralDomainObject> entityList, int batchSize) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.saveBatch(entityList, batchSize);
    }

    /**
     * @see #saveOrUpdateBatch(Long, Collection, int)
     */
    default boolean saveOrUpdateBatch(Long boId, Collection<GeneralDomainObject> entityList) throws BizException {
        return saveOrUpdateBatch(boId, entityList, -1);
    }

    /**
     * 批量保存或者更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     * @return true if success
     */
    default boolean saveOrUpdateBatch(Long boId, Collection<GeneralDomainObject> entityList, int batchSize) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * @see #removeById(Long, Serializable, boolean)
     */
    default boolean removeById(Long boId, Serializable id) throws BizException {
        return removeById(boId, id, false);
    }

    /**
     * 根据id删除
     *
     * @param id      主键(类型必须与实体类型字段保持一致)
     * @param useFill 是否启用填充(为true的情况,会将入参转换实体进行delete删除)
     * @return true if success
     */
    default boolean removeById(Long boId, Serializable id, boolean useFill) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.removeById(id, useFill);
    }

    /**
     * 根据实体的id移除记录
     *
     * @param entity 实体
     * @return true if success
     */
    default boolean removeById(Long boId, GeneralDomainObject entity) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.removeById(entity);
    }

    /**
     * 根据给定的字段-值 map关系移除记录
     *
     * @param columnMap 表字段 map 对象
     * @return true if success
     */
    default boolean removeByMap(Long boId, Map<String, Object> columnMap) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.removeByMap(columnMap);
    }

    /**
     * 根据条件移除记录员
     *
     * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return true if success
     */
    default boolean remove(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.remove(queryWrapper);
    }

    /**
     * @see #removeByIds(Long, Collection, boolean)
     */
    default boolean removeByIds(Long boId, Collection<?> list) throws BizException {
        return removeByIds(boId, list, false);
    }

    /**
     * id集合删除
     *
     * @param list    主键ID或实体列表
     * @param useFill 是否填充(为true的情况,会将入参转换实体进行delete删除)
     * @return true if success
     */
    default boolean removeByIds(Long boId, Collection<?> list, boolean useFill) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.removeByIds(list, useFill);
    }

    /**
     * @see #removeBatchByIds(Long, Collection, int, boolean)
     */
    default boolean removeBatchByIds(Long boId, Collection<?> list) throws BizException {
        return removeBatchByIds(boId, list, -1, false);
    }

    /**
     * @see #removeBatchByIds(Long, Collection, int, boolean)
     */
    default boolean removeBatchByIds(Long boId, Collection<?> list, boolean useFill) throws BizException {
        return removeBatchByIds(boId, list, -1, useFill);
    }

    /**
     * @see #removeBatchByIds(Long, Collection, int, boolean)
     */
    default boolean removeBatchByIds(Long boId, Collection<?> list, int batchSize) throws BizException {
        return removeBatchByIds(boId, list, batchSize, false);
    }

    /**
     * 根据id批量删除
     *
     * @param list      主键ID或实体列表
     * @param batchSize 批次大小
     * @param useFill   是否启用填充(为true的情况,会将入参转换实体进行delete删除)
     * @return true if success
     */
    default boolean removeBatchByIds(Long boId, Collection<?> list, int batchSize, boolean useFill) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.removeBatchByIds(list, batchSize, useFill);
    }

    /**
     * 根据实体的id更新实体对象
     *
     * @param entity 实体对象
     * @return true if success
     */
    default boolean updateById(Long boId, GeneralDomainObject entity) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.updateById(entity);
    }

    /**
     * 根据条件设置实体对象
     *
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     * @return true if success
     */
    default boolean update(Long boId, Wrapper<GeneralDomainObject> updateWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.update(updateWrapper);
    }

    /**
     * 根据实体对象与wrapper条件更新实体
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     * @return true if success
     */
    default boolean update(Long boId, GeneralDomainObject entity, Wrapper<GeneralDomainObject> updateWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.update(entity, updateWrapper);
    }

    /**
     * @see #updateBatchById(Long, Collection, int)
     */
    default boolean updateBatchById(Long boId, Collection<GeneralDomainObject> entityList) throws BizException {
        return updateBatchById(boId, entityList, -1);
    }

    /**
     * 批量更新实体
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     * @return true if success
     */
    default boolean updateBatchById(Long boId, Collection<GeneralDomainObject> entityList, int batchSize) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.updateBatchById(entityList, batchSize);
    }

    /**
     * save or update
     *
     * @param entity 实体对象
     * @return true if success
     */
    default boolean saveOrUpdate(Long boId, GeneralDomainObject entity) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.saveOrUpdate(entity);
    }

    /**
     * 根据主键获取实体
     *
     * @param id 主键ID
     * @return entity
     */
    default GeneralDomainObject getById(Long boId, Serializable id) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getById(id);
    }

    /**
     * 根据主键获取实体
     *
     * @param id 主键ID
     * @return entity
     */
    default Optional<GeneralDomainObject> getOptById(Long boId, Serializable id) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getOptById(id);
    }

    /**
     * 根据ids获取实体列表
     *
     * @param idList 主键ID列表
     * @return entity list
     */
    default List<GeneralDomainObject> listByIds(Long boId, Collection<? extends Serializable> idList) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listByIds(idList);
    }

    /**
     * 根据columnMap获取实体列表
     *
     * @param columnMap 表字段 map 对象
     * @return list entity
     */
    default List<GeneralDomainObject> listByMap(Long boId, Map<String, Object> columnMap) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listByMap(columnMap);
    }

    /**
     * @see #getOne(Long, Wrapper, boolean)
     */
    default GeneralDomainObject getOne(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        return getOne(boId, queryWrapper, false);
    }

    /**
     * get one by condition
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     * @return entity or throw
     */
    default GeneralDomainObject getOne(Long boId, Wrapper<GeneralDomainObject> queryWrapper, boolean throwEx) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getOne(queryWrapper, throwEx);
    }

    /**
     * @see #getOneOpt(Long, Wrapper, boolean)
     */
    default Optional<GeneralDomainObject> getOneOpt(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        return getOneOpt(boId, queryWrapper, false);
    }

    /**
     * get one
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     * @return entity or throw
     */
    default Optional<GeneralDomainObject> getOneOpt(Long boId, Wrapper<GeneralDomainObject> queryWrapper, boolean throwEx) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getOneOpt(queryWrapper, throwEx);
    }

    /**
     * get map
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return map entity
     */
    default Map<String, Object> getMap(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getMap(queryWrapper);
    }

    /**
     * get obj
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper       转换函数
     * @param <V>          v
     * @return v
     */
    default <V> V getObj(Long boId, Wrapper<GeneralDomainObject> queryWrapper, Function<? super Object, V> mapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.getObj(queryWrapper, mapper);
    }

    /**
     * exists
     *
     * @param queryWrapper queryWrapper
     * @return true if success
     */
    default boolean exists(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.exists(queryWrapper);
    }

    /**
     * count
     *
     * @return count
     */
    default long count(Long boId) throws BizException {
        return count(boId, Wrappers.emptyWrapper());
    }

    /**
     * count
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return count
     */
    default long count(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.count(queryWrapper);
    }

    /**
     * @see #list(Long, IPage, Wrapper)
     */
    default List<GeneralDomainObject> list(Long boId) throws BizException {
        return list(boId, Wrappers.emptyWrapper());
    }

    /**
     * @see #list(Long, IPage, Wrapper)
     */
    default List<GeneralDomainObject> list(Long boId, IPage<GeneralDomainObject> page) throws BizException {
        return list(boId, page, Wrappers.emptyWrapper());
    }

    /**
     * @see #list(Long, IPage, Wrapper)
     */
    default List<GeneralDomainObject> list(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        return list(boId, null, queryWrapper);
    }

    /**
     * list
     *
     * @param page         分页条件
     * @param queryWrapper queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return list entity
     */
    default List<GeneralDomainObject> list(Long boId, IPage<GeneralDomainObject> page, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.list(page, queryWrapper);
    }

    /**
     * page
     *
     * @param page 翻页对象
     * @return page entity
     */
    default <E extends IPage<GeneralDomainObject>> E page(Long boId, E page) throws BizException {
        return page(boId, page, Wrappers.emptyWrapper());
    }

    /**
     * page
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return page entity
     */
    default <E extends IPage<GeneralDomainObject>> E page(Long boId, E page, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.page(page, queryWrapper);
    }

    /**
     * @see #listMaps(Long, IPage, Wrapper)
     */
    default List<Map<String, Object>> listMaps(Long boId) throws BizException {
        return listMaps(boId, null, Wrappers.emptyWrapper());
    }

    /**
     * @see #listMaps(Long, IPage, Wrapper)
     */
    default List<Map<String, Object>> listMaps(Long boId, IPage<? extends Map<String, Object>> page) throws BizException {
        return listMaps(boId, page, Wrappers.emptyWrapper());
    }

    /**
     * @see #listMaps(Long, IPage, Wrapper)
     */
    default List<Map<String, Object>> listMaps(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        return listMaps(boId, null, queryWrapper);
    }

    /**
     * list map
     *
     * @param page         分页条件
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return list map
     */
    default List<Map<String, Object>> listMaps(Long boId, IPage<? extends Map<String, Object>> page, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listMaps(page, queryWrapper);
    }

    /**
     * list obj
     *
     * @return list obj
     */
    default <E> List<E> listObjs(Long boId) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listObjs();
    }

    /**
     * list obj
     *
     * @param mapper 转换函数
     * @param <V>
     * @return list obj
     */
    default <V> List<V> listObjs(Long boId, Function<? super Object, V> mapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listObjs(mapper);
    }

    /**
     * list obj
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param <E>
     * @return list obj
     */
    default <E> List<E> listObjs(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listObjs(queryWrapper);
    }

    /**
     * list obj
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper       转换函数
     * @param <V>
     * @return list obj
     */
    default <V> List<V> listObjs(Long boId, Wrapper<GeneralDomainObject> queryWrapper, Function<? super Object, V> mapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.listObjs(queryWrapper, mapper);
    }

    /**
     * @see #pageMaps(Long, IPage, Wrapper)
     */
    default <E extends IPage<Map<String, Object>>> E pageMaps(Long boId, E page) throws BizException {
        return pageMaps(boId, page, Wrappers.emptyWrapper());
    }

    /**
     * page map
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param <E>
     * @return page map
     */
    default <E extends IPage<Map<String, Object>>> E pageMaps(Long boId, E page, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.pageMaps(page, queryWrapper);
    }

    /**
     * tree
     *
     * @param queryWrapper 查询条件
     * @return tree
     */
    default List<GeneralDomainObject> tree(Long boId, Wrapper<GeneralDomainObject> queryWrapper) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.tree(queryWrapper);
    }

    /**
     * tree
     *
     * @param queryWrapper 查询条件
     * @param treeType     领域树类型
     * @param <Z>          Tree entity
     * @return tree
     */
    default <Z extends TreeDomain<GeneralDomainObject, Z>> List<Z> tree(Long boId, Wrapper<GeneralDomainObject> queryWrapper, Class<Z> treeType) throws BizException {
        IDomainService<GeneralDomainObject> boRepository = getBoRepositoryOrThrow(boId);
        return boRepository.tree(queryWrapper, treeType);
    }

    /**
     * 基于获取业务对象Repository
     *
     * @return ITurboCrudTreeRepositoryService
     * @throws BizException 如果为null则抛出
     */
    IDomainService<GeneralDomainObject> getBoRepositoryOrThrow(Long boId) throws BizException;

}
