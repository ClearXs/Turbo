package cc.allio.turbo.common.domain;

import cc.allio.uno.core.api.Self;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * possess multi {@link BehaviorObservable}
 *
 * @author j.x
 * @date 2024/8/27 17:33
 * @since 0.1.1
 */
public class MultiObservable<D> implements Observable<D>, Self<MultiObservable<D>> {

    private final List<Observable<D>> observableList;

    @SafeVarargs
    public MultiObservable(Observable<D>... observables) {
        this.observableList = Arrays.asList(observables);
    }

    /**
     * concat {@link BehaviorObservable} to multi observable.
     * <p>refer to {@link Flux#concat(Publisher[])}</p>
     *
     * @param observables the {@link BehaviorObservable} list
     * @return the current instance
     */
    @SafeVarargs
    public final MultiObservable<D> concat(Observable<D>... observables) {
        if (observables != null && observables.length > 0) {
            var concatList = Arrays.asList(observables);
            this.observableList.addAll(concatList);
        }
        return self();
    }

    @Override
    public Mono<Subscription<D>> observe() {
        return Flux.fromIterable(observableList).flatMap(Observable::observe).singleOrEmpty();
    }

    @Override
    public Flux<Subscription<D>> observeMany() {
        return Flux.fromIterable(observableList).flatMap(Observable::observeMany);
    }
}
