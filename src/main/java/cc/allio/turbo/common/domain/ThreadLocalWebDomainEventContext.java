package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * <p><b>解决领域行为异步订阅时web域数据的丢失问题</b></p>
 * 基于{@link ThreadLocal}的带有Web属性{@link DomainEventContext}
 *
 * @author j.x
 * @date 2024/2/1 17:10
 * @since 0.1.0
 */
public class ThreadLocalWebDomainEventContext extends DomainEventContext {

    private static final ThreadLocal<ThreadLocalWebDomainEventContext> EVENT_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    // web数据
    private static final String TENANT_ID_KEY = "tenant_id_key";
    private static final String TOKEN_KEY = "token_key";
    private static final String REQUEST_KEY = "request_key";

    public ThreadLocalWebDomainEventContext(DomainEventContext domainEventContext) {
        super(domainEventContext.subscription, domainEventContext.behavior);
        putAll(domainEventContext.getAll());
    }

    public ThreadLocalWebDomainEventContext(Subscriber<?> subscription, Method behavior) {
        super(subscription, behavior);
        Optional.ofNullable(WebUtil.getTenant()).ifPresent(tenantId -> put(TENANT_ID_KEY, tenantId));
        Optional.ofNullable(WebUtil.getToken()).ifPresent(token -> put(TOKEN_KEY, token));
        Optional.ofNullable(WebUtil.getRequest()).ifPresent(request -> put(REQUEST_KEY, request));
    }

    /**
     * 获取当前web域的tenant_id
     *
     * @return tenant_id
     */
    public Optional<String> getTenantId() {
        return get(TENANT_ID_KEY, String.class);
    }

    /**
     * 获取当前web域的token
     *
     * @return token
     */
    public Optional<String> getToken() {
        return get(TOKEN_KEY, String.class);
    }

    /**
     * 获取当前web域的request
     *
     * @return request
     */
    public Optional<HttpServletRequest> getRequest() {
        return get(REQUEST_KEY, HttpServletRequest.class);
    }

    /**
     * 获取当前线程的领域事件上下文
     *
     * @return DomainEventContext or null
     */
    public static ThreadLocalWebDomainEventContext getCurrentThreadContext() {
        return EVENT_CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置领域上下文信息
     *
     * @param context context
     */
    public static void setEventContextThreadLocal(ThreadLocalWebDomainEventContext context) {
        EVENT_CONTEXT_THREAD_LOCAL.set(context);
    }

    /**
     * 清除当前ThreadLocal数据
     */
    public static void remove() {
        EVENT_CONTEXT_THREAD_LOCAL.remove();
    }
}
