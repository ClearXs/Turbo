package cc.allio.turbo.common.event;

import java.util.concurrent.atomic.AtomicBoolean;

public class Watcher {

    private final Subscriber<?> subscriber;
    private final AtomicBoolean completion;

    public Watcher(Subscriber<?> subscriber) {
        this.subscriber = subscriber;
        this.completion = new AtomicBoolean(false);
    }

}
