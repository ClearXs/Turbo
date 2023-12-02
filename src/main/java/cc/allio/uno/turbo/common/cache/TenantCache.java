package cc.allio.uno.turbo.common.cache;

import cc.allio.uno.turbo.common.util.WebUtil;

/**
 * 在缓存中设置租户号
 *
 * @author j.x
 * @date 2023/10/27 14:49
 * @since 1.0.0
 */
public interface TenantCache<T> extends TurboCache<T> {

    /**
     * 获取租户号
     */
    default String getTenant() {
        return WebUtil.getTenant();
    }
}
