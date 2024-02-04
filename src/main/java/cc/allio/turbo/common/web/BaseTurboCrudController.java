package cc.allio.turbo.common.web;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.web.params.QueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * CRUD接口的抽象定义
 *
 * @param <T> 实体类型
 * @param <D> 领域类型，用于与实体类型进行相互转换
 * @author jiangwei
 * @date 2024/2/4 22:22
 * @see cc.allio.turbo.common.domain.Domains
 * @since 0.1.0
 */
public abstract class BaseTurboCrudController<T extends Entity, D extends Entity> extends TurboController {

    /**
     * 保存
     *
     * @param domain 领域对象
     * @return if true success
     */
    public abstract R<Boolean> save(@Validated @RequestBody D domain);

    /**
     * 编辑
     *
     * @param domain 领域对象
     * @return if true success
     */
    public abstract R<Boolean> edit(@Validated @RequestBody D domain);

    /**
     * 保存或者更新
     *
     * @param domain 领域对象
     * @return if true success
     */
    public abstract R<Boolean> saveOrUpdate(@Validated @RequestBody D domain);

    /**
     * 批量保存
     *
     * @param domain 领域对象
     * @return if true success
     */
    public abstract R<Boolean> batchSave(@Validated @RequestBody List<D> domain);

    /**
     * 批量删除
     *
     * @param ids 对象ids
     * @return if true success
     */
    public abstract R<Boolean> delete(@RequestBody List<Serializable> ids);

    /**
     * 详情
     *
     * @param id id
     * @return 领域对象实例
     */
    public abstract R<D> details(Serializable id);

    /**
     * 列表
     *
     * @param params 查询参数
     * @return 领域对象列表
     */
    public abstract R<List<D>> list(@RequestBody QueryParam<T> params);

    /**
     * 分页
     *
     * @param params 查询参数
     * @return 领域对象分页列表
     */
    public abstract R<IPage<D>> page(@RequestBody QueryParam<T> params);

    /**
     * 导出
     *
     * @param params 查询参数
     */
    public abstract void export(@RequestBody QueryParam<T> params, HttpServletResponse response);

    /**
     * 导入
     *
     * @param file file
     */
    public abstract R<Boolean> importFile(MultipartFile file);

}
