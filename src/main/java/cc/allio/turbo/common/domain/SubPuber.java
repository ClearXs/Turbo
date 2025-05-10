package cc.allio.turbo.common.domain;

/**
 * combine {@link Subscriber} and {@link Publisher}
 *
 * @author j.x
 * @since 0.2.0
 */
public interface SubPuber<D> extends Subscriber<D>, Publisher<D> {
}
