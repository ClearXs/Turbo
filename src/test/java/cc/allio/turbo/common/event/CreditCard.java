package cc.allio.turbo.common.event;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@RequiredArgsConstructor
public class CreditCard implements Subscriber<Double> {


    @NonNull
    private DomainEventBus domainEventBus;

    @NonNull
    private Double balance;

    public double pay(double money) {
        log.info("Paying with credit card: {}", money);
        balance = balance - money;
        log.info("New balance: {}", balance);
        return balance;
    }
}
