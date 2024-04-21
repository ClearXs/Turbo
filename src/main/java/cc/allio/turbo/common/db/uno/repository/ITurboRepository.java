package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.IPage;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Repository marker interface，Anywhere domain object Operate Collection of base Interface in System. Base on <b>uno-data</b>
 *
 * @author j.x
 * @date 2024/1/23 23:42
 * @since 0.1.0
 */
public interface ITurboRepository<T extends Entity> extends EntityTypeAware<T> {

    /**
     * 插入数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean insert(UnaryOperator<InsertOperator<?>> func) {
        return getExecutor().insert(getEntityType(), func);
    }

    /**
     * 更新数据
     * <p>根据给定{@link UpdateOperator}更新数据</p>
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean update(UnaryOperator<UpdateOperator<?>> func) {
        return getExecutor().update(getEntityType(), func);
    }

    /**
     * 删除数据
     * <p>根据给定的条件删除数据</p>
     *
     * @param func func
     * @return true 成功 false 失败
     */
    default boolean delete(UnaryOperator<DeleteOperator<?>> func) {
        return getExecutor().delete(func);
    }

    /**
     * 查询一个数据
     *
     * @param func the func
     * @return 实体 or null
     */
    default T queryOne(UnaryOperator<QueryOperator<?>> func) {
        return getExecutor().queryOne(getEntityType(), func);
    }

    /**
     * 查询list实体
     *
     * @param func the func
     * @return list
     */
    default List<T> queryList(UnaryOperator<QueryOperator<?>> func) {
        return getExecutor().queryList(getEntityType(), func);
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<T> queryPage(IPage<?> page, UnaryOperator<QueryOperator<?>> func) {
        return getExecutor().queryPage(page, func, getEntityType());
    }

    /**
     * 获取CommandExecutor实例
     *
     * @return CommandExecutor
     */
    default AggregateCommandExecutor getExecutor() {
        return DSExtractor.extract(this.getClass());
    }

    /**
     * 获取实体类型
     *
     * @return t class
     */
    default Class<T> getEntityType() {
        return (Class<T>) ReflectTools.getGenericType(this, ITurboRepository.class, 0);
    }
}
