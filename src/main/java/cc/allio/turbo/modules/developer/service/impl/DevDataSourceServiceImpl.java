package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.developer.constant.StorageType;
import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.turbo.modules.developer.mapper.DevDataSourceMapper;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DevDataSourceServiceImpl
        extends TurboCrudServiceImpl<DevDataSourceMapper, DevDataSource>
        implements IDevDataSourceService, DisposableBean {

    private final CommandExecutorContext commandExecutorContext;
    private final List<Interceptor> interceptors;

    public DevDataSourceServiceImpl(CommandExecutorContext commandExecutorContext, List<Interceptor> interceptors) {
        this.commandExecutorContext = commandExecutorContext;
        this.interceptors = interceptors;
    }

    @Override
    public DevDataSource createByExecutorOptions(ExecutorOptions executorOptions) {
        DevDataSource devDataSource = new DevDataSource();
        devDataSource.setKey(executorOptions.getKey());
        devDataSource.setEngine(StorageType.getByDbType(executorOptions.getDbType()));
        devDataSource.setName(executorOptions.getDbType().getName());
        devDataSource.setUsername(executorOptions.getUsername());
        devDataSource.setPassword(executorOptions.getPassword());
        devDataSource.setDatabase(executorOptions.getDatabase());
        devDataSource.setAddress(executorOptions.getAddress());
        devDataSource.setDefaulted(executorOptions.isSystemDefault());
        return devDataSource;
    }

    @Override
    public boolean testConnection(DevDataSource dataSource) {
        Long id = dataSource.getId();
        CommandExecutor commandExecutor = null;
        if (id == null) {
            ExecutorOptions executorOptions = this.createExecutorOptions(dataSource);
            if (executorOptions == null) {
                return false;
            }
            commandExecutor = commandExecutorContext.crate(executorOptions);
        } else {
            commandExecutor = getCommandExecutor(id);
        }
        if (commandExecutor == null) {
            return false;
        }
        try {
            return commandExecutor.check();
        } catch (Throwable ex) {
            log.error("execute options {} connection failed", JsonUtils.toJson(dataSource), ex);
            return false;
        }
    }

    @Override
    public boolean createTables(Long dataSourceId, TableColumns... tableColumns) {
        if (tableColumns != null && tableColumns.length > 0) {
            CommandExecutor commandExecutor = getCommandExecutor(dataSourceId);
            if (commandExecutor != null) {
                return Arrays.stream(tableColumns)
                        .allMatch(tableColumn -> commandExecutor.createTable(o -> o.from(tableColumn.getTable()).columns(tableColumn.getColumnDefs().toArray(ColumnDef[]::new))));
            }
        }
        return false;
    }

    @Override
    public boolean dropTables(Long dataSourceId, String... tableNames) {
        if (tableNames != null && tableNames.length > 0) {
            CommandExecutor commandExecutor = getCommandExecutor(dataSourceId);
            if (commandExecutor != null) {
                return Arrays.stream(tableNames)
                        .allMatch(commandExecutor::dropTable);
            }
        }

        return false;
    }

    @Override
    public CommandExecutor getCommandExecutor(Long dataSourceId) {
        String key = commandExecutorContext.lockGet(dataSourceId);
        return commandExecutorContext.getCommandExecutor(key);
    }

    @Override
    public List<TableColumns> showTables(Long dataSourceId, Table... tables) {
        CommandExecutor commandExecutor = getCommandExecutor(dataSourceId);
        if (commandExecutor == null) {
            return Collections.emptyList();
        }
        List<Table> showTables =
                commandExecutor.showTables(o -> {
                    if (tables != null) {
                        Arrays.stream(tables).forEach(o::from);
                    }
                    return o;
                });
        return showTables.stream()
                .map(table -> {
                    TableColumns tableColumns = new TableColumns(table);
                    List<ColumnDef> columnDefs = commandExecutor.showColumns(table);
                    tableColumns.addAllColumns(columnDefs);
                    return tableColumns;
                })
                .toList();

    }

    @Override
    public ExecutorOptions createExecutorOptions(DevDataSource dataSource) {
        DBType dbType = dataSource.getEngine().toDbType();
        ExecutorKey executorKey = null;
        OperatorKey operatorKey = null;
        if (DBType.DBCategory.RELATIONAL == dbType.getCategory()) {
            executorKey = ExecutorKey.DB;
            operatorKey = OperatorKey.SQL;
        } else if (DBType.ELASTIC_SEARCH == dbType) {
            executorKey = ExecutorKey.ELASTICSEARCH;
            operatorKey = OperatorKey.ELASTICSEARCH;
        } else if (DBType.REDIS == dbType) {
            executorKey = ExecutorKey.REDIS;
            operatorKey = OperatorKey.REDIS;
        } else if (DBType.MONGODB == dbType) {
            executorKey = ExecutorKey.MONGODB;
            operatorKey = OperatorKey.MONGODB;
        } else if (DBType.INFLUXDB == dbType) {
            executorKey = ExecutorKey.INFLUXDB;
            operatorKey = OperatorKey.INFLUXDB;
        } else if (DBType.NEO4J == dbType) {
            executorKey = ExecutorKey.NEO4j;
            operatorKey = OperatorKey.NEO4j;
        }
        if (executorKey == null) {
            return null;
        }
        return ExecutorOptionsBuilder
                .create(dbType, dataSource.getKey())
                .executorKey(executorKey)
                .operatorKey(operatorKey)
                .address(dataSource.getAddress())
                .username(dataSource.getUsername())
                .password(dataSource.getPassword())
                .database(dataSource.getDatabase())
                .systemDefault(dataSource.isDefaulted())
                .interceptors(interceptors)
                .build();
    }

    @Override
    public void destroy() throws Exception {
        commandExecutorContext.clear();
    }
}
