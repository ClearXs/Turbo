package cc.allio.turbo.modules.ai.chat.message;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamMessageTest extends BaseTestCase {

    @Test
    void testMultiThreadObserve() {

        StreamMessage streamMessage = new StreamMessage();

        AtomicInteger count = new AtomicInteger(0);
        CompletableFuture<Void> f2 =
                CompletableFuture.runAsync(() -> streamMessage.observe().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));

        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> streamMessage.observe().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));


        assertDoesNotThrow(() -> CompletableFuture.allOf(f1, f2).get());


        streamMessage.feed(new MessageImpl());

        assertEquals(2, count.get());
    }

    @Test
    void testMultiThreadUntilFinish() {
        StreamMessage streamMessage = new StreamMessage();

        AtomicInteger count = new AtomicInteger(0);
        CompletableFuture<Void> f2 =
                CompletableFuture.runAsync(() -> streamMessage.observe().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));

        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> streamMessage.observe().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));

        CompletableFuture<Void> f3 =
                CompletableFuture.runAsync(() -> streamMessage.collect().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));

        assertDoesNotThrow(() -> CompletableFuture.allOf(f1, f2, f3).get());

        MessageImpl message = new MessageImpl();
        message.setFinish(AdvancedMessage.FINISH_STOP);
        streamMessage.feed(message);

        assertEquals(3, count.get());
    }


    @Test
    void testComplete() {
        StreamMessage streamMessage = new StreamMessage();

        AtomicInteger count = new AtomicInteger(0);
        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> streamMessage.observe().subscribe(s -> count.incrementAndGet()), Executors.newFixedThreadPool(1));

        assertDoesNotThrow(() -> CompletableFuture.allOf(f1).get());

        streamMessage.feed(new MessageImpl());

        assertEquals(1, count.get());

        streamMessage.complete();

        streamMessage.feed(new MessageImpl());

        assertEquals(1, count.get());
    }

    @Test
    void testTemporalTriggerListener() {

        Mono.from(
                        Flux.from(StreamMessage.from("hello"))
                                .collectList()
                                .flatMap(StreamMessage::fromOthers)
                )
                .flatMapMany(StreamMessage::observe)
                .take(1)
                .map(AdvancedMessage::content)
                .as(StepVerifier::create)
                .expectNext("hello")
                .verifyComplete();


    }
}
