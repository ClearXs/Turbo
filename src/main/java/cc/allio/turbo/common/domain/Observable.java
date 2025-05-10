package cc.allio.turbo.common.domain;

import lombok.NonNull;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * domain behavior observer, Encapsulation{@link org.reactivestreams.Subscription}
 *
 * @author j.x
 * @date 2024/8/27 17:40
 * @since 0.1.1
 */
public interface Observable<D> {

    /**
     * @see #observeOnConsummation(Consumer, UnaryOperator)
     */
    default Disposable observeOnConsummation(Consumer<Subscription<D>> acceptor) {
        return observeOnConsummation(acceptor, ThreadLocalWebDomainEventContext::new);
    }

    /**
     * execute observeOnConsummation.
     *
     * @param acceptor when exiting active observeOnConsummation callback.
     * @return the {@link Disposable} instance
     * @see #observeMany()
     */
    default Disposable observeOnConsummation(Consumer<Subscription<D>> acceptor, UnaryOperator<DomainEventContext> eventContext) {
        return observeMany(eventContext).subscribe(acceptor);
    }

    /**
     * @see #observe(UnaryOperator)
     */
    default Mono<Subscription<D>> observe() {
        return observe(ThreadLocalWebDomainEventContext::new);
    }

    /**
     * observeOnConsummation many domain behavior
     *
     * @return the flux
     */
    default Flux<Subscription<D>> observeMany() {
        return observeMany(ThreadLocalWebDomainEventContext::new)
                .onErrorContinue((err, obj) -> ThreadLocalWebDomainEventContext.remove())
                .doOnComplete(ThreadLocalWebDomainEventContext::remove);
    }

    /**
     * observeOnConsummation domain behavior
     *
     * @param refineEventContext when subscribe element emit the allow user refine event context, and add more features
     *                           like use {@link ThreadLocalWebDomainEventContext} to solve the problem of losing web domain data when asynchronous subscription
     * @return the mono
     */
    Mono<Subscription<D>> observe(UnaryOperator<DomainEventContext> refineEventContext);

    /**
     * observeOnConsummation many domain behavior
     *
     * @param refineEventContext when subscribe element emit the allow user refine event context, and add more features
     *                           like use {@link ThreadLocalWebDomainEventContext} to solve the problem of losing web domain data when asynchronous subscription
     * @return the flux
     */
    Flux<Subscription<D>> observeMany(UnaryOperator<DomainEventContext> refineEventContext);

    /**
     * basic on {@link Mono} create new {@link Observable}
     *
     * @param fromMono the {@link Mono} instance
     * @param <D>      the domain type
     * @return {@link Observable}
     */
    static <D> Observable<D> from(@NonNull Mono<D> fromMono) {
        return new MonoObservable<>(fromMono);
    }

    /**
     * basis on {@link Flux} create new {@link Observable}
     *
     * @param fromFlux the {@link Flux} instance
     * @param <D>      the domain type
     * @return {@link Observable}
     */
    static <D> Observable<D> from(@NonNull Flux<D> fromFlux) {
        return new FluxObservable<>(fromFlux);
    }

    class MonoObservable<D> implements Observable<D> {

        private final Mono<D> mono;

        public MonoObservable(Mono<D> mono) {
            this.mono = mono;
        }

        @Override
        public Mono<Subscription<D>> observe(UnaryOperator<DomainEventContext> refineEventContext) {
            return mono.map(domain -> () -> Optional.ofNullable(domain));
        }

        @Override
        public Flux<Subscription<D>> observeMany(UnaryOperator<DomainEventContext> refineEventContext) {
            return Flux.from(observe());
        }
    }

    class FluxObservable<D> implements Observable<D> {

        private final Flux<D> flux;

        public FluxObservable(Flux<D> flux) {
            this.flux = flux;
        }

        @Override
        public Mono<Subscription<D>> observe(UnaryOperator<DomainEventContext> refineEventContext) {
            return observeMany().take(1).single();
        }

        @Override
        public Flux<Subscription<D>> observeMany(UnaryOperator<DomainEventContext> refineEventContext) {
            return flux.map(domain -> () -> Optional.ofNullable(domain));
        }
    }
}
