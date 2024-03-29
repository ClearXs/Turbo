package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.web.params.QueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 当使用{@link TurboCrudController}时，对其每一个方法进行后置拦截。
 *
 * @param <T> 实体类型
 * @param <D> 领域类型
 * @param <S> 实体对应service类型
 * @author j.x
 * @date 2024/1/18 18:41
 * @since 0.1.0
 */
public interface WebCrudAfterInterceptor<T extends Entity, D extends Entity, S extends ITurboCrudService<T>> {

    /**
     * 在{@link TurboCrudController#save(Entity)}在之后进行调用
     *
     * @param service service
     * @param entity  entity
     * @param saved   是否保存成功
     */
    default void onSaveAfter(S service, T entity, boolean saved) {
    }

    /**
     * 在{@link TurboCrudController#edit(Entity)}之后进行调用
     *
     * @param service service
     * @param entity  entity
     * @param edited  是否更新成功
     */
    default void onEditAfter(S service, T entity, boolean edited) {
    }

    /**
     * * 在{@link BaseTurboCrudController#delete(List)} }之前进行调用
     *
     * @param service service
     * @param deleted 是否删除成功
     */
    default void onDeleteAfter(S service, boolean deleted) {
    }

    /**
     * 在{@link TurboCrudController#saveOrUpdate(Entity)}之后进行调用
     *
     * @param service       service
     * @param entity        entity
     * @param saveOrUpdated 保存是否成功
     */
    default void onSaveOrUpdateAfter(S service, T entity, boolean saveOrUpdated) {
    }

    /**
     * 在{@link TurboCrudController#batchSave(List)}之后进行调用
     *
     * @param service service
     * @param entity  entity
     * @param saved   保存是否成功
     */
    default void onBatchSaveAfter(S service, List<T> entity, boolean saved) {
    }

    /**
     * * 在{@link TurboCrudController#details(long)}之后进行调用
     *
     * @param service service
     * @param entity  entity
     */
    default D onDetailsAfter(S service, T entity) {
        return (D) entity;
    }

    /**
     * *在{@link TurboCrudController#list(QueryParam)}之后进行调用
     *
     * @param service service
     * @param entity  entity
     * @param params  params
     * @return domain entity
     */
    default List<D> onListAfter(S service, List<T> entity, QueryParam<T> params) {
        return (List<D>) entity;
    }

    /**
     * * 在{@link TurboCrudController#page(QueryParam)}之后进行调用
     *
     * @param service service
     * @param entity  entity
     * @param params  params
     * @return domain page
     */
    default IPage<D> onPageAfter(S service, IPage<T> entity, QueryParam<T> params) {
        return (IPage<D>) entity;
    }


    /**
     * 在{@link TurboCrudController#export(QueryParam, HttpServletResponse)}之后进行调用
     *
     * @param service  service
     * @param response response
     * @param domain   domain list
     */
    default void onExportAfter(S service, HttpServletResponse response, List<D> domain) {
    }

    /**
     * * 在{@link TurboCrudController#importFile(MultipartFile)}之后进行调用
     *
     * @param service service
     */
    default void onImportAfter(S service) {
    }
}
