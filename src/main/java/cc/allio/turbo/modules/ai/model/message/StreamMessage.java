package cc.allio.turbo.modules.ai.model.message;

import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import com.google.common.collect.Lists;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;

/**
 * when use {@link ExecutionMode#STREAM} mode. this conversation message save to current.
 * <p>
 * design use for {@link FluxSink} and listener mechanism. it is resolve multi-user can't synchronous observe stream message.
 * <p>
 * such as when user observe message stream, and other user feed message to stream. the observer can't receive message.
 * <p>
 * so through listener mechanism and adopt sink source resolve multi-user observe stream message.
 *
 * @author j.x
 * @since 0.2.0
 */
public class StreamMessage {

    FluxSink<Message> source;
    private final List<SinkListener> listeners;

    public StreamMessage() {
        this.listeners = Lists.newCopyOnWriteArrayList();
        Flux.<Message>push(sink -> {
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
    public void feed(Message message) {
        if (source != null && !source.isCancelled()) {
            source.next(message);
            if (Message.FINISH_STOP.equals(message.finish())) {
                source.complete();
            }
        }
    }

    /**
     * collect {@link Message} until receive finish message.
     */
    public Mono<List<Message>> collect() {
        return Flux.create(this::addListener, FluxSink.OverflowStrategy.LATEST)
                .bufferUntil(message -> Message.FINISH_STOP.equals(message.finish()))
                .take(1)
                .single();
    }

    /**
     * observe {@link Message} stream.
     */
    public Flux<Message> observe() {
        return Flux.create(this::addListener, FluxSink.OverflowStrategy.LATEST);
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
     * add listener
     *
     * @param sink the sink
     */
    void addListener(FluxSink<Message> sink) {
        SinkListener listener =
                new SinkListener() {
                    @Override
                    public void accept(Message message) {
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
    }

    @FunctionalInterface
    interface SinkListener extends Consumer<Message> {

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
