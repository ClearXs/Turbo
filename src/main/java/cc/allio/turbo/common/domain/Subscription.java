package cc.allio.turbo.common.domain;

import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * definition  subscriber.
 *
 * @param <D> the domain type
 */
public interface Subscription<D> {

    /**
     * get domain instance of optional
     *
     * @return the optional of {@link D}
     */
    Optional<D> getDomain();

    /**
     * transform {@link #getDomain()} to {@link Mono}
     *
     * @return the {@link Mono} of {@link D}
     */
    default Mono<D> getMonoDomain() {
        return Mono.justOrEmpty(getDomain());
    }

    /**
     * create default {@link Subscription}
     *
     * @param domain the domain instance
     * @param <D>    domain type
     * @return
     */
    static <D> Subscription<D> of(@Nullable D domain) {
        return () -> Optional.ofNullable(domain);
    }
}
