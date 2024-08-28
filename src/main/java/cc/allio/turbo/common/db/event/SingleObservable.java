package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.bus.event.Node;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * 领域行为观察者，封装{@link org.reactivestreams.Subscription}
 *
 * @author j.x
 * @date 2024/1/26 18:57
 * @since 0.1.1
 */
@Slf4j
public class SingleObservable<D> implements Observable<D> {

    protected final Subscriber<D> subscriber;
    protected final String eventBehavior;
    protected final DomainEventBus eventBus;

    public SingleObservable(Subscriber<D> subscriber, String eventBehavior, DomainEventBus eventBus) {
        this.subscriber = subscriber;
        this.eventBehavior = eventBehavior;
        this.eventBus = eventBus;
    }

    @Override
    public Disposable observe(Consumer<Subscription<D>> acceptor) {
        return doObserve(acceptor).subscribe();
    }

    /**
     * do observe domain subscribe behavior
     *
     * @param acceptor the acceptor
     * @return
     */
    Flux<ThreadLocalWebDomainEventContext> doObserve(Consumer<Subscription<D>> acceptor) {
        return Mono.justOrEmpty(eventBus)
                .flatMapMany(bus ->
                        bus.subscribeOnRepeatable(eventBehavior)
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(Node::onNext)
                                .map(context -> {
                                    ThreadLocalWebDomainEventContext threadLocalContext = new ThreadLocalWebDomainEventContext(context);
                                    ThreadLocalWebDomainEventContext.setEventContextThreadLocal(threadLocalContext);
                                    return threadLocalContext;
                                })
                                .doOnNext(context -> {
                                    Method behavior = context.getBehavior();
                                    Subscription<D> subscription = new Subscription<>(subscriber, eventBehavior, behavior, context);
                                    acceptor.accept(subscription);
                                })
                                .doOnNext(context -> ThreadLocalWebDomainEventContext.remove())
                                .onErrorContinue((err, obj) -> {
                                    log.warn("domain behavior {} leading to exception, now capture this error, then continue", eventBehavior, err);
                                    ThreadLocalWebDomainEventContext.remove();
                                })
                );
    }
}
