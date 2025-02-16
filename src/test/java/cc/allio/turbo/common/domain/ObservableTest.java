package cc.allio.turbo.common.domain;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;

public class ObservableTest extends BaseTestCase {

    @Test
    void testMonoObserve() {
        Observable.from(Mono.just("1"))
                .observe()
                .flatMap(Subscription::getMonoDomain)
                .as(StepVerifier::create)
                .expectNext("1")
                .verifyComplete();

        // observe many

        Observable.from(Mono.just("1"))
                .observeMany()
                .flatMap(Subscription::getMonoDomain)
                .as(StepVerifier::create)
                .expectNext("1")
                .verifyComplete();
    }

    @Test
    void testFluxObserver() {

        Observable.from(Flux.empty())
                .observe()
                .flatMap(Subscription::getMonoDomain)
                .as(StepVerifier::create)
                .expectError(NoSuchElementException.class)
                .verify();


        Flux<String> source = Flux.just("1", "2");

        Observable.from(source)
                .observe()
                .flatMap(Subscription::getMonoDomain)
                .as(StepVerifier::create)
                .expectNext("1")
                .verifyComplete();

        Observable.from(source)
                .observeMany()
                .flatMap(Subscription::getMonoDomain)
                .as(StepVerifier::create)
                .expectNext("1")
                .expectNext("2")
                .verifyComplete();

    }

    // Test case for MultiObservable

    @Test
    void testNonParametersConstruct() {
        MultiObservable<Object> observable = new MultiObservable<>();

        observable.observeMany()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void testMixObserver() {
        MultiObservable<String> observable = new MultiObservable<>();

        observable.concat(
                        Observable.from(Mono.just("1")),
                        Observable.from(Flux.just("2", "3"))
                )
                .observeMany()
                .map(Subscription::getDomain)
                .flatMap(Mono::justOrEmpty)
                .as(StepVerifier::create)
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();
    }

}
