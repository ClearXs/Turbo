package cc.allio.turbo.common.db.uno.repository;

import cc.allio.uno.data.orm.executor.CommandExecutor;

/**
 * Repository marker interface，Anywhere domain object Operate Collection of base Interface in System. Base on <b>uno-data</b>
 *
 * @author jiangwei
 * @date 2024/1/23 23:42
 * @since 0.1.0
 */
public interface ITurboRepository {

    /**
     * 获取CommandExecutor实例
     *
     * @return CommandExecutor
     */
    CommandExecutor getExecutor();
}
