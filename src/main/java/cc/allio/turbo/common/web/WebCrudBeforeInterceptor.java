package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.domain.Domains;
import cc.allio.turbo.common.web.params.QueryParam;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 当使用{@link TurboCrudController}时，对其每一个方法进行前置拦截。
 *
 * @param <T> 实体类型
 * @param <D> 领域类型
 * @param <S> 实体对应service类型
 * @author jiangwei
 * @date 2024/1/18 18:41
 * @since 0.1.0
 */
public interface WebCrudBeforeInterceptor<T extends Entity, D extends Entity, S extends ITurboCrudService<T>> {

    /**
     * 在{@link TurboCrudController#edit(Entity)}之前进行调用
     *
     * @param service service
     * @param domain  domain
     * @return 实体类型T
     */
    default T onEditBefore(S service, D domain) {
        return Domains.toEntity(domain, service.getEntityClass());
    }

    /**
     * 在{@link TurboCrudController#save(Entity)}在之前进行调用
     *
     * @param service entity service
     * @param domain  domain
     * @return 实体类型T
     */
    default T onSaveBefore(S service, D domain) {
        return Domains.toEntity(domain, service.getEntityClass());
    }

    /**
     * 在{@link TurboCrudController#delete(List)} }之前进行调用
     *
     * @param service service
     * @param ids     ids
     */
    default void onDeleteBefore(S service, List<Serializable> ids) {
    }

    /**
     * 在{@link TurboCrudController#saveOrUpdate(Entity)}之前进行调用
     *
     * @param service service
     * @param domain  domain
     * @return entity
     */
    default T onSaveOrUpdateBefore(S service, D domain) {
        return Domains.toEntity(domain, service.getEntityClass());
    }

    /**
     * 在{@link TurboCrudController#batchSave(List)}之前进行调用
     *
     * @param service service
     * @param domains domains
     * @return entity for list
     */
    default List<T> onBatchSaveBefore(S service, List<D> domains) {
        return domains.stream().map(domain -> Domains.toEntity(domain, service.getEntityClass())).toList();
    }

    /**
     * 在{@link TurboCrudController#details(Serializable)}之前进行调用
     *
     * @param service service
     * @param id      id
     */
    default void onDetailsBefore(S service, Serializable id) {
    }

    /**
     * 在{@link TurboCrudController#list(QueryParam)}之前进行调用
     *
     * @param service service
     * @param params  params
     */
    default void onListBefore(S service, QueryParam<T> params) {
    }

    /**
     * 在{@link TurboCrudController#page(QueryParam)}之前进行调用
     *
     * @param service service
     * @param params  params
     */
    default void onPageBefore(S service, QueryParam<T> params) {
    }

    /**
     * 在{@link TurboCrudController#export(QueryParam, HttpServletResponse)}之前进行调用
     *
     * @param service service
     * @param entity  entity
     */
    default List<D> onExportBefore(S service, List<T> entity) {
        return (List<D>) entity;
    }

    /**
     * * 在{@link TurboCrudController#importFile(MultipartFile)}之前进行调用
     *
     * @param service service
     * @param file    file
     */
    default void onImportBefore(S service, MultipartFile file) {
    }
}
