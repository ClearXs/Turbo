package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * act on {@link Subscriber}
 *
 * @author j.x
 * @date 2024/8/27 15:20
 * @since 0.1.1
 */
public class DomainBeanPostProcessor implements BeanPostProcessor {

    private final EventBus<DomainEventContext> eventBus;

    public DomainBeanPostProcessor(EventBus<DomainEventContext> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // judgement bean is Subscriber
        if (bean instanceof Subscriber<?> subscriber) {
            subscriber.setDomainEventBus(eventBus);
        }
        return bean;
    }
}
