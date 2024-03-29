package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.bus.event.Node;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * 领域行为观察者，封装{@link org.reactivestreams.Subscription}
 *
 * @author j.x
 * @date 2024/1/26 18:57
 * @since 1.1.6
 */
@Slf4j
public class Observable<D> {

    private final Subscriber<D> subscriber;
    private final String eventBehavior;

    @Setter
    private DomainEventBus eventBus;

    @Setter
    private Method behavior;

    public Observable(Subscriber<D> subscriber, String eventBehavior) {
        this.subscriber = subscriber;
        this.eventBehavior = eventBehavior;
    }

    /**
     * 触发行为领域行为订阅
     *
     * @param acceptor 订阅信息消费
     */
    public Disposable observe(Consumer<Subscription<D>> acceptor) {
        if (eventBus != null) {
            return eventBus.subscribeOnRepeatable(eventBehavior)
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(Node::onNext)
                    .map(context -> {
                        ThreadLocalWebDomainEventContext threadLocalContext = new ThreadLocalWebDomainEventContext(context);
                        ThreadLocalWebDomainEventContext.setEventContextThreadLocal(threadLocalContext);
                        return threadLocalContext;
                    })
                    .doOnNext(context -> {
                        Subscription<D> subscription = new Subscription<>(subscriber, eventBehavior, behavior, context);
                        acceptor.accept(subscription);
                    })
                    .doOnNext(context -> ThreadLocalWebDomainEventContext.remove())
                    .onErrorContinue((err, obj) -> {
                        log.warn("domain behavior {} leading to exception, now capture this error, then continue", eventBehavior, err);
                        ThreadLocalWebDomainEventContext.remove();
                    })
                    .subscribe();
        }
        return Flux.empty().subscribe();
    }
}
