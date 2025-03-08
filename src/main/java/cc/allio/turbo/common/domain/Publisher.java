package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.Topic;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.function.lambda.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * domain event publisher.
 *
 * @param <D> event
 * @author j.x
 * @since 0.2.0
 */
public interface Publisher<D> extends InitializingBean, DisposableBean, Domain<D> {

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodConsumer<T> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodSupplier<T> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T, R> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodFunction<T, R> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodBiConsumer<T1, T2> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2, R> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodBiFunction<T1, T2, R> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2, T3> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodTerConsumer<T1, T2, T3> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2, T3, R> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodTerFunction<T1, T2, T3, R> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2, T3, T4> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodQueConsumer<T1, T2, T3, T4> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * @see #publishOn(String, DomainEventContext)
     */
    default <T1, T2, T3, T4, R> Flux<Topic<DomainEventContext>> publishOn(ThrowingMethodQueFunction<T1, T2, T3, T4, R> eventMethod, DomainEventContext context) {
        String eventName = eventMethod.getMethodName();
        return publishOn(eventName, context);
    }

    /**
     * publish event to {@link EventBus}
     *
     * @param event   the event
     * @param context the event context
     * @return
     */
    default Flux<Topic<DomainEventContext>> publishOn(String event, DomainEventContext context) {
        return publishOn(TopicKey.of(event), context);
    }

    /**
     * publish event to {@link EventBus}
     *
     * @param path    the topic path key
     * @param context the event context
     * @return
     */
    default Flux<Topic<DomainEventContext>> publishOn(TopicKey path, DomainEventContext context) {
        // like DomainName/event
        TopicKey domainPath = buildEventPath(path);
        return getDomainEventBus().publish(domainPath, context);
    }

    /**
     * batch publish to {@link EventBus}
     *
     * @param paths   the list of {@link TopicKey}
     * @param context the {@link DomainEventContext}
     * @return
     */
    default Flux<Topic<DomainEventContext>> publishOn(List<TopicKey> paths, DomainEventContext context) {
        return getDomainEventBus()
                .batchPublish(
                        // append event name for each path
                        paths.stream().map(this::buildEventPath).toList(),
                        context
                );
    }
}
