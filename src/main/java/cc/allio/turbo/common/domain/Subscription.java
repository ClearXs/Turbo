package cc.allio.turbo.common.domain;

import java.util.Optional;

/**
 * definition  subscriber.
 *
 * @param <D> the domain type
 */
public interface Subscription<D> {

    /**
     * get domain instance of optional
     */
    Optional<D> getDomain();
}
