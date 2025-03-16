package cc.allio.turbo.modules.ai.chat.message;

import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * when use {@link ExecutionMode#STREAM} mode. this conversation message save to current.
 * <p>
 * design use for {@link FluxSink} and listener mechanism. it is resolve multi-user can't synchronous observeOnConsummation stream message.
 * <p>
 * such as when user observeOnConsummation message stream, and other user feed message to stream. the observer can't receive message.
 * <p>
 * so through listener mechanism and adopt sink source resolve multi-user observeOnConsummation stream message.
 *
 * @author j.x
 * @since 0.2.0
 */
public class StreamMessage {

    FluxSink<AdvancedMessage> source;
    private final List<SinkListener> listeners;
    private final Queue<AdvancedMessage> temporal;

    private AtomicBoolean isObserve = new AtomicBoolean(false);

    public StreamMessage() {
        this.listeners = Lists.newCopyOnWriteArrayList();
        this.temporal = Queues.newConcurrentLinkedQueue();
        Flux.<AdvancedMessage>push(sink -> {
                    this.source = sink;
                    // cancel all listener
                    sink.onCancel(() -> {
                        listeners.forEach(SinkListener::onCancel);
                        listeners.clear();
                    });
                    // disposal all listener
                    sink.onDispose(() -> {
                        listeners.forEach(SinkListener::onDispose);
                        listeners.clear();
                    });
                })
                .subscribe(message -> listeners.forEach(listener -> listener.accept(message)));
    }

    /**
     * feed to stream
     *
     * @param message th
     */
    public void feed(AdvancedMessage message) {
        if (source != null && !source.isCancelled()) {

            if (isObserve.get()) {
                source.next(message);
            } else {
                // ensure element can be able to observeOnConsummation
                temporal.offer(message);
            }

            if (isObserve.get() && AdvancedMessage.FINISH_STOP.equals(message.finish())) {
                source.complete();
            }
        }
    }

    /**
     * collect {@link AdvancedMessage} until receive finish message.
     */
    public Mono<List<AdvancedMessage>> collect() {
        return Flux.create(this::addListener, FluxSink.OverflowStrategy.BUFFER)
                .bufferUntil(message -> AdvancedMessage.FINISH_STOP.equals(message.finish()))
                .take(1)
                .single();
    }

    /**
     * observeOnConsummation {@link AdvancedMessage} stream.
     */
    public Flux<AdvancedMessage> observe() {
        return Flux.create(this::addListener, FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * complete stream
     */
    public void complete() {
        if (source != null) {
            source.complete();
        }
    }

    /**
     * source is cancel
     *
     * @return true if cancel
     * @see FluxSink#isCancelled()
     */
    public boolean isCancel() {
        return source != null && source.isCancelled();
    }

    /**
     * concat other {@link StreamMessage}
     */
    public void concat(StreamMessage... streamMessages) {
        if (streamMessages != null) {
            concat(Lists.newArrayList(streamMessages));
        }
    }

    /**
     * concat other {@link StreamMessage}
     */
    public void concat(List<StreamMessage> streamMessages) {
        Flux.fromIterable(streamMessages)
                .flatMap(StreamMessage::observe)
                .subscribe(this::feed);
    }

    /**
     * @see #from(List)
     */
    public static Mono<StreamMessage> from(String... texts) {
        if (texts == null) {
            return Mono.empty();
        }
        return from(
                Arrays.stream(texts)
                        .map(text -> {
                            MessageImpl message = new MessageImpl();
                            message.setContent(text);
                            return message;
                        })
                        .toList()
        );
    }

    /**
     * @see #from(List)
     */
    public static <T extends AdvancedMessage> Mono<StreamMessage> from(T... messages) {
        return from(Lists.newArrayList(messages));
    }

    /**
     * create {@link StreamMessage} from {@link AdvancedMessage}
     */
    public static <T extends AdvancedMessage> Mono<StreamMessage> from(List<T> messages) {
        StreamMessage streamMessage = new StreamMessage();
        return Mono.defer(() ->
                Mono.just(streamMessage)
                        .doOnSubscribe(subscription -> {
                            for (AdvancedMessage message : messages) {
                                streamMessage.feed(message);
                            }
                        })
        );
    }

    public static Mono<StreamMessage> fromOthers(List<StreamMessage> others) {
        StreamMessage streamMessage = new StreamMessage();
        return Mono.just(streamMessage).doOnSubscribe(subscription -> streamMessage.concat(others));
    }

    /**
     * add listener
     *
     * @param sink the sink
     */
    void addListener(FluxSink<AdvancedMessage> sink) {
        SinkListener listener =
                new SinkListener() {
                    @Override
                    public void accept(AdvancedMessage message) {
                        sink.next(message);
                    }

                    @Override
                    public void onCancel() {
                        sink.complete();
                    }

                    @Override
                    public void onDispose() {
                        sink.complete();
                    }
                };

        sink.onCancel(() -> listeners.remove(listener));

        sink.onDispose(() -> listeners.remove(listener));

        listeners.add(listener);

        // ensure emit temporal message
        if (isObserve.compareAndSet(false, true)) {
            emitTemporal();
        }

    }

    void emitTemporal() {
        AdvancedMessage message;
        while ((message = temporal.poll()) != null) {
            source.next(message);
        }
    }

    @FunctionalInterface
    interface SinkListener extends Consumer<AdvancedMessage> {

        /**
         * @see FluxSink#onDispose(Disposable)
         */
        default void onDispose() {

        }

        /**
         * @see FluxSink#onCancel(Disposable)
         */
        default void onCancel() {

        }
    }
}
