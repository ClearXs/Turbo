package cc.allio.turbo.common.domain;

import cc.allio.uno.core.bus.EventBus;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@RequiredArgsConstructor
public class CreditCard implements Subscriber<Double> {

    @NonNull
    private EventBus<DomainEventContext> domainEventBus;

    @NonNull
    private Double balance;

    public double pay(double money) {
        log.info("Paying with credit card: {}", money);
        balance = balance - money;
        log.info("New balance: {}", balance);
        return balance;
    }
}
