package cc.allio.turbo.common.domain;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.bus.EventBus;
import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.bus.Watcher;
import cc.allio.uno.core.bus.Watchers;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * cohesion subscribe publish and watch tools.
 * <p>
 * fast carry on {@link Subscriber} and {@link Publisher} operations.
 * <p>
 * event bus use for {@link EventBusFactory#current()}
 * <p>
 * integrate {@link Subscriber} and {@link Publisher} and {@link Watcher}
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public final class DomainTools<D> implements SubPuber<D>, Watcher<DomainEventContext>, Self<DomainTools<D>> {

    private final Domain<D> actual;
    private Watcher<DomainEventContext> watcher;

    DomainTools(Domain<D> actual) {
        this.actual = actual;
    }

    @Override
    public void setDomainEventBus(EventBus<DomainEventContext> eventBus) {
        // nothing
        actual.setDomainEventBus(eventBus);
    }

    @Override
    public EventBus<DomainEventContext> getDomainEventBus() {
        return EventBusFactory.current();
    }

    @Override
    public String getDomainName() {
        return Optional.ofNullable(actual)
                .map(Domain::getDomainName)
                .orElse(SubPuber.super.getDomainName());
    }

    @Override
    public Watcher<DomainEventContext> watch(String path) {
        String newPath = buildEventPath(path);
        this.watcher = Watchers.watch(getDomainEventBus(), newPath);
        return self();
    }

    @Override
    public UntilComplete<DomainEventContext> untilComplete(Runnable trigger, Runnable executable) {
        return Optional.ofNullable(watcher)
                .map(w -> w.untilComplete(trigger, executable))
                .orElseThrow(() -> new UnsupportedOperationException("call untilComplete method before acquire call watch method."));
    }

    /**
     * delegate require actual {@link Domain} instance. expand it availability.
     *
     * @param actual the actual instance.
     * @param <D>    the Domain type
     * @return DomainFeature
     */
    public static <D> DomainTools<D> fromActual(Domain<D> actual) {
        if (actual == null) {
            throw new NullPointerException("actual must be not null.");
        }
        return new DomainTools<>(actual);
    }
}
