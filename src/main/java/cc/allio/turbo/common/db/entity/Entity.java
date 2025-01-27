package cc.allio.turbo.common.db.entity;

import cc.allio.uno.core.util.concurrent.LockContext;

import java.io.Serializable;

public interface Entity {

    ThreadLocal<LockContext> LOCAL_LOCK_CONTEXT = new ThreadLocal<>();

    /**
     * 获取Id
     */
    Serializable getId();

    /**
     * 设置{@link LockContext}
     *
     * @param lockContext lockContext
     */
    static void putThreadLockContext(LockContext lockContext) {
        LOCAL_LOCK_CONTEXT.set(lockContext);
    }

    /**
     * 获取{@link LockContext}}
     */
    static LockContext obtainThreadLockContext() {
        return LOCAL_LOCK_CONTEXT.get();
    }

    /**
     * 清除
     */
    static void clearThreadLockContext() {
        LOCAL_LOCK_CONTEXT.remove();
    }
}
