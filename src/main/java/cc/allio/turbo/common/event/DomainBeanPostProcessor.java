package cc.allio.turbo.common.event;

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

    private final DomainEventBus eventBus;

    public DomainBeanPostProcessor(DomainEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // judgement bean is Subscriber
        if (bean instanceof Subscriber<?> subscriber) {
            subscriber.setDomainEventBus(eventBus);
        }
        return bean;
    }
}
