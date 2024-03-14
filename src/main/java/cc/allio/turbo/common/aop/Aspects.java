package cc.allio.turbo.common.aop;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 基于{@link AspectJProxyFactory}快捷创建Proxy对象
 *
 * @author j.x
 * @date 2024/2/7 17:11
 * @since 0.1.0
 */
public final class Aspects {

    /**
     * 获取{@link AspectJProxyBuilder}实例
     *
     * @return AspectJProxyBuilder
     */
    public static AspectJProxyBuilder builder() {
        return new AspectJProxyBuilder();
    }

    /**
     * 基于目标对象与{@link TurboAdvisor}advisor创建代理对象
     *
     * @param target   target
     * @param advisors advisors
     * @return proxy instance
     */
    public static <T> T create(Object target, TurboAdvisor... advisors) {
        return builder()
                .target(target)
                .addAdvisors(advisors)
                .proxy();
    }

    /**
     * 基于目标对象与{@link TurboAdvisor}advisor创建代理对象
     *
     * @param target   target
     * @param advisors advisors
     * @return proxy instance
     */
    public static <T> T create(Object target, Collection<TurboAdvisor> advisors) {
        return builder()
                .target(target)
                .addAdvisors(advisors)
                .proxy();
    }

    public static class AspectJProxyBuilder implements Self<AspectJProxyBuilder> {

        private Object target;
        private final List<TurboAdvisor> advisors = Lists.newArrayList();
        private final List<Class<?>> interfaces = Lists.newArrayList();

        /**
         * 设置目标对象
         *
         * @param target 目标对象
         * @return {@code this}
         */
        public AspectJProxyBuilder target(Object target) {
            this.target = target;
            return self();
        }

        /**
         * 添加Advisor
         */
        public AspectJProxyBuilder addAdvisor(TurboAdvisor advisor) {
            this.advisors.add(advisor);
            return self();
        }

        /**
         * 添加Advisor
         */
        public AspectJProxyBuilder addAdvisors(TurboAdvisor... advisors) {
            return addAdvisors(Arrays.stream(advisors).toList());
        }

        /**
         * 添加Advisor
         */
        public AspectJProxyBuilder addAdvisors(Collection<TurboAdvisor> advisors) {
            if (CollectionUtils.isNotEmpty(advisors)) {
                this.advisors.addAll(advisors);
            }
            return self();
        }

        /**
         * 添加 interface
         */
        public AspectJProxyBuilder addInterface(Class<?> itf) {
            this.interfaces.add(itf);
            return self();
        }

        /**
         * 生成代理对象。
         * <p>如果经过{@link #addAdvisors(Collection)}存在{@link #advisors}则会创建代理对象，否则返回原始对象</p>
         *
         * @return proxy
         */
        public <T> T proxy() {
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            proxyFactory.setTarget(target);
            proxyFactory.addAspect(CommonAspect.class);
            for (TurboAdvisor advisor : advisors) {
                advisor.preProxyProcess(target);
                proxyFactory.addAdvisor(advisor);
            }
            for (Class<?> itf : interfaces) {
                proxyFactory.addInterface(itf);
            }
            T proxy = proxyFactory.getProxy(getClass().getClassLoader());
            for (TurboAdvisor advisor : advisors) {
                advisor.postProxyProcess(proxy, target);
            }
            return proxy;
        }
    }
}
