package cc.allio.turbo.modules.office.documentserver.callbacks;

import cc.allio.turbo.modules.office.documentserver.vo.Track;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * callback event bus
 *
 * @author j.x
 * @date 2024/5/27 19:52
 * @since 0.2
 */
@Slf4j
public class CallbackEventBus {

    private static final AtomicLong COUNTER = new AtomicLong(0L);
    static Map<Long, Subscriber> subscriberList = Maps.newConcurrentMap();

    /**
     * publish {@link Track} to all {@link Subscriber}
     *
     * @param track the {@link Track} instance
     */
    public static void publish(Track track) {
        Collection<Subscriber> subscribers = subscriberList.values();
        for (Subscriber subscriber : subscribers) {
            subscriber.set(track);
        }
    }

    /**
     * subscribe to {@link Track}
     *
     * @return the {@link Future}
     */
    public static Subscriber subscribe() {
        long count = COUNTER.getAndIncrement();
        Subscriber subscriber = new Subscriber(count);
        subscriberList.put(count, subscriber);
        return subscriber;
    }

    /**
     * remove subscribe
     *
     * @param subscriber the {@link Subscriber} instance
     */
    public static void remove(Subscriber subscriber) {
        Long key = subscriber.getKey();
        subscriberList.remove(key);
    }

    public static class Subscriber implements Future<Track> {

        @Getter
        private final Long key;
        final Lock internalLock = new ReentrantLock();
        private final Condition condition;
        private Track track;
        private final AtomicBoolean cancel;

        public Subscriber(Long key) {
            this.key = key;
            this.condition = internalLock.newCondition();
            this.cancel = new AtomicBoolean(false);
        }

        /**
         * set callback {@link Track} instance
         *
         * @param track the {@link Track} instance
         */
        public void set(Track track) {
            internalLock.lock();
            try {
                this.track = track;
                condition.signalAll();
            } finally {
                internalLock.unlock();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            internalLock.lock();
            try {
                condition.signalAll();
                cancel.compareAndSet(false, true);
            } finally {
                internalLock.unlock();
            }
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancel.get();
        }

        @Override
        public boolean isDone() {
            return cancel.get();
        }

        @Override
        public Track get() throws InterruptedException, ExecutionException {
            try {
                return get(10L, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                log.error("get callback timeout.", ex);
                return null;
            }
        }

        @Override
        public Track get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (track != null) {
                return track;
            }
            internalLock.lock();
            try {
                boolean await = condition.await(timeout, unit);
                if (!await) {
                    return null;
                }
                return track;
            } finally {
                // will be remove this subscriber
                remove(this);
                internalLock.unlock();
            }
        }
    }
}
