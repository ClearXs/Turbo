package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ExecutorOptions;

import java.util.List;

public interface IDevDataSourceService extends ITurboCrudService<DevDataSource> {

    /**
     * 测试连接
     *
     * @param dataSource dataSource
     */
    boolean testConnection(DevDataSource dataSource);

    /**
     * 创建表
     *
     * @param dataSourceId dataSourceId
     * @param tableColumns tableColumns
     * @return
     */
    boolean createTables(Long dataSourceId, TableColumns... tableColumns);

    /**
     * 删除表
     *
     * @param dataSourceId dataSourceId
     * @param tableNames   tableNames
     * @return true if success
     */
    boolean dropTables(Long dataSourceId, String... tableNames);

    /**
     * 基于数据源id根据{@link CommandExecutor}实例
     *
     * @param dataSourceId dataSourceId
     * @return CommandExecutor
     */
    CommandExecutor getCommandExecutor(Long dataSourceId);

    /**
     * 根据数据源id获取{@link Table}数据表
     *
     * @param dataSourceId dataSourceId
     * @param tables       table查询
     * @return tables
     */
    List<TableColumns> showTables(Long dataSourceId, Table... tables);

    /**
     * 基于{@link DevDataSource}创建{@link ExecutorOptions}实例
     *
     * @param dataSource dataSource
     * @return ExecutorOptions or null
     */
    ExecutorOptions createExecutorOptions(DevDataSource dataSource);

    /**
     * 基于{@link ExecutorOptions}创建{@link DevDataSource}
     *
     * @param executorOptions executorOptions
     * @return DevDataSource
     */
    DevDataSource createByExecutorOptions(ExecutorOptions executorOptions);
}
