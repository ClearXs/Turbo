package cc.allio.turbo.common.domain;

import lombok.NonNull;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * domain behavior observer, Encapsulation{@link org.reactivestreams.Subscription}
 *
 * @author j.x
 * @date 2024/8/27 17:40
 * @since 0.1.1
 */
public interface Observable<D> {

    /**
     * execute observe.
     *
     * @param acceptor when exiting active observe callback.
     * @return the {@link Disposable} instance
     * @see #observeMany()
     */
    default Disposable observe(Consumer<Subscription<D>> acceptor) {
        return observeMany().subscribe(acceptor);
    }

    /**
     * observe domain behavior
     *
     * @return the mono
     */
    Mono<Subscription<D>> observe();

    /**
     * observe many domain behavior
     *
     * @return the flux
     */
    Flux<Subscription<D>> observeMany();

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
        public Mono<Subscription<D>> observe() {
            return mono.map(domain -> () -> Optional.ofNullable(domain));
        }

        @Override
        public Flux<Subscription<D>> observeMany() {
            return Flux.from(observe());
        }
    }

    class FluxObservable<D> implements Observable<D> {

        private final Flux<D> flux;

        public FluxObservable(Flux<D> flux) {
            this.flux = flux;
        }

        @Override
        public Mono<Subscription<D>> observe() {
            return observeMany().take(1).single();
        }

        @Override
        public Flux<Subscription<D>> observeMany() {
            return flux.map(domain -> () -> Optional.ofNullable(domain));
        }
    }
}
