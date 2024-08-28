package cc.allio.turbo.common.db.event;

import cc.allio.uno.core.api.Self;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * possess multi {@link SingleObservable}
 *
 * @author j.x
 * @date 2024/8/27 17:33
 * @since 0.1.1
 */
public class MultiObservable<D> implements Observable<D>, Self<MultiObservable<D>> {

    private final List<SingleObservable<D>> observableList;

    @SafeVarargs
    public MultiObservable(SingleObservable<D>... observables) {
        if (observables == null || observables.length == 0) {
            throw new IllegalArgumentException();
        }
        this.observableList = Arrays.asList(observables);
    }

    /**
     * concat {@link SingleObservable} to multi observable.
     * <p>refer to {@link Flux#concat(Publisher[])}</p>
     *
     * @param observables the {@link SingleObservable} list
     * @return the current instance
     */
    @SafeVarargs
    public final MultiObservable<D> concat(SingleObservable<D>... observables) {
        if (observables != null && observables.length > 0) {
            var concatList = Arrays.asList(observables);
            this.observableList.addAll(concatList);
        }
        return self();
    }

    @Override
    public Disposable observe(Consumer<Subscription<D>> acceptor) {
        return Flux.concat(
                        observableList.stream()
                                .map(observable -> observable.doObserve(acceptor))
                                .toList()
                )
                .subscribe();
    }
}
