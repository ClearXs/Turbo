package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.TopicKey;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.Method;

/**
 * 领域行为观察者，封装{@link org.reactivestreams.Subscription}
 *
 * @author j.x
 * @date 2024/1/26 18:57
 * @since 0.1.1
 */
@Slf4j
public class BehaviorObservable<D> implements Observable<D> {

    protected final Subscriber<D> subscriber;
    protected final TopicKey eventBehavior;
    protected final EventBus<DomainEventContext> eventBus;

    public BehaviorObservable(Subscriber<D> subscriber,
                              TopicKey eventBehavior,
                              EventBus<DomainEventContext> eventBus) {
        this.subscriber = subscriber;
        this.eventBehavior = eventBehavior;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Subscription<D>> observe() {
        return doObserve().single();
    }

    @Override
    public Flux<Subscription<D>> observeMany() {
        return doObserve();
    }

    /**
     * do observe domain subscribe behavior
     */
    Flux<Subscription<D>> doObserve() {
        return Mono.justOrEmpty(eventBus)
                .flatMapMany(bus ->
                        bus.subscribeOnRepeatable(eventBehavior)
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(context -> {
                                    ThreadLocalWebDomainEventContext threadLocalContext = new ThreadLocalWebDomainEventContext(context);
                                    ThreadLocalWebDomainEventContext.setEventContextThreadLocal(threadLocalContext);
                                    return threadLocalContext;
                                })
                                .map(context -> {
                                    Method behavior = context.getBehavior();
                                    return new BehaviorSubscription<>(subscriber, eventBehavior, behavior, context);
                                })
                                .onErrorContinue((err, obj) -> {
                                    log.warn("domain behavior {} leading to exception, now capture this error, then continue", eventBehavior, err);
                                    ThreadLocalWebDomainEventContext.remove();
                                })
                                .doOnComplete(ThreadLocalWebDomainEventContext::remove)
                );
    }
}
