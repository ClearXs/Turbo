package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.constant.StorageType;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * extract {@link DS#value()} and convert {@link AggregateCommandExecutor}
 *
 * @author j.x
 * @date 2024/3/28 23:22
 * @since 0.1.1
 */
public final class DSExtractor {

    /**
     * Extract {@link ITurboRepository} whether it has annotation {@link DS} base on {@link DBType}. exists vary {@link AggregateCommandExecutor}, fetch first one. without return default
     *
     * @param serviceType the serviceType
     * @return AggregateCommandExecutor instance
     */
    public static AggregateCommandExecutor extract(Class<? extends ITurboRepository> serviceType) {
        DS ds = AnnotationUtils.findAnnotation(serviceType, DS.class);
        if (ds == null) {
            return CommandExecutorFactory.getDSLExecutor();
        }
        StorageType storageType = ds.value();
        DBType dbType = DBType.getDbType(storageType.getValue());
        if (dbType == null) {
            return CommandExecutorFactory.getDSLExecutor();
        }
        AggregateCommandExecutor commandExecutor = CommandExecutorFactory.getDSLExecutorByDbType(dbType);
        if (commandExecutor == null) {
            throw Exceptions.unNull(String.format("Failed to base on %s find command executor. ", dbType.getName()));
        }
        return commandExecutor;
    }
}
