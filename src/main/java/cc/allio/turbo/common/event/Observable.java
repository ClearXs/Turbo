package cc.allio.turbo.common.event;

import reactor.core.Disposable;

import java.util.function.Consumer;

/**
 *
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
     */
    Disposable observe(Consumer<Subscription<D>> acceptor);
}
