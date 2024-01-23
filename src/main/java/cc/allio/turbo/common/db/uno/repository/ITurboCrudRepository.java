package cc.allio.turbo.common.db.uno.repository;

import cc.allio.uno.core.util.ReflectTool;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 定义Repository Crud操作集合
 *
 * @author jiangwei
 * @date 2024/1/23 23:47
 * @since 0.1.0
 */
public interface ITurboCrudRepository<T, ID> extends ITurboRepository {

    /**
     * 插入数据
     *
     * @param pojo pojo
     * @return true 成功 false 失败
     * @see CommandExecutor#insertPojo(Object)
     */
    default boolean insert(T pojo) {
        return getExecutor().insertPojo(pojo);
    }

    /**
     * 插入数据（认定为同一个表数据）
     *
     * @param pojos pojos
     * @return true 成功 false 失败
     */
    default boolean batchInsert(List<T> pojos) {
        return getExecutor().insertPojo(pojos);
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default boolean update(T pojo) {
        return getExecutor().updatePojo(pojo);
    }

    /**
     * 更新数据根据条件
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default boolean update(T pojo, UnaryOperator<WhereOperator<UpdateOperator>> condition) {
        return getExecutor().updatePojoByCondition(pojo, condition);
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default boolean updateById(T pojo, ID id) {
        return getExecutor().updatePojoById(pojo, id);
    }

    /**
     * 根据id删除数据
     *
     * @param id id
     * @return true 成功 false 失败
     */
    default boolean deleteById(Serializable id) {
        return getExecutor().deleteById(getEntityType(), id);
    }

    default Class<T> getEntityType() {
        return (Class<T>) ReflectTool.getGenericType(this, ITurboCrudRepository.class, 0);
    }
}
