package cc.allio.turbo.modules.ai.exception;

import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.modules.ai.driver.model.Input;
import lombok.Getter;

public class DispatchException extends Exception {

    @Getter
    private final Subscription<Input> subscription;

    public DispatchException(Subscription<Input> subscription, Throwable cause) {
        super(cause);
        this.subscription = subscription;
    }

    public DispatchException(Subscription<Input> subscription, String cause) {
        super(cause);
        this.subscription = subscription;
    }
}
