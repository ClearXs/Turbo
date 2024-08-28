package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.turbo.common.aop.TurboAdvisorBuilder;
import cc.allio.uno.core.function.lambda.MethodPredicate;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 领域行为切面
 *
 * @author j.x
 * @date 2024/1/26 17:13
 * @see BehaviorAdvisorBuilder#builder(Class, MethodPredicate)
 * @since 0.1.0
 */
@Slf4j
public class BehaviorAdvisor extends TurboAdvisor {

    private final DomainEventBus domainEventBus;

    public BehaviorAdvisor(DomainEventBus domainEventBus) {
        super();
        this.domainEventBus = domainEventBus;
    }

    /**
     * @param bean bean
     */
    @Override
    public void preProxyProcess(Object bean) {
        MethodInterceptor methodInterceptor = new BehaviorMethodInterceptor((Subscriber<?>) bean, domainEventBus);
        setAdvice(methodInterceptor);
    }

    /**
     * BehaviorAdvisor 构建
     */
    public static class BehaviorAdvisorBuilder extends TurboAdvisorBuilder<BehaviorAdvisor> {

        protected BehaviorAdvisorBuilder() {
            super(BehaviorAdvisor.class, target -> Subscriber.class.isAssignableFrom(target.getClass()));
        }

        /**
         * 获取构造器实例
         *
         * @return TurboAdvisorBuilder
         */
        public static TurboAdvisorBuilder<BehaviorAdvisor> builder(DomainEventBus domainEventBus) {
            BehaviorAdvisorBuilder behaviorAdvisorBuilder = new BehaviorAdvisorBuilder();
            return behaviorAdvisorBuilder.constructParameters(new Object[]{domainEventBus});
        }
    }
}
