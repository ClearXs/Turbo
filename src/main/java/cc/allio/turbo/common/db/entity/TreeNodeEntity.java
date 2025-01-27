package cc.allio.turbo.common.db.entity;

import cc.allio.uno.core.util.tree.Expand;

import java.io.Serializable;

/**
 * tree结点标识
 *
 * @author j.x
 * @date 2024/1/19 00:02
 * @since 0.1.0
 */
public interface TreeNodeEntity extends Entity, Expand {

    /**
     * 获取Id
     */
    Serializable getParentId();
}
