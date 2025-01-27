package cc.allio.turbo.common.domain;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @see #observe()
     */
    default Disposable observe(Consumer<Subscription<D>> acceptor) {
        return observe().subscribe(acceptor);
    }

    /**
     * observe domain behavior
     *
     * @return the mono
     */
    Mono<Subscription<D>> observe();

    /**
     * observe
     * @return
     */
    Flux<Subscription<D>> observeMany();
}
