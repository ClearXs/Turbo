package cc.allio.turbo.common.cache;

import cc.allio.turbo.common.db.event.ThreadLocalWebDomainEventContext;
import cc.allio.turbo.common.util.WebUtil;

import java.util.Optional;

/**
 * 在缓存中设置租户号
 *
 * @author j.x
 * @date 2023/10/27 14:49
 * @since 0.1.0
 */
public interface TenantCache extends TurboCache {

    /**
     * 获取租户号
     */
    default String getTenant() {
        ThreadLocalWebDomainEventContext currentThreadContext = ThreadLocalWebDomainEventContext.getCurrentThreadContext();
        return Optional.ofNullable(currentThreadContext)
                .flatMap(ThreadLocalWebDomainEventContext::getTenantId)
                .orElse(WebUtil.getTenant());
    }
}
