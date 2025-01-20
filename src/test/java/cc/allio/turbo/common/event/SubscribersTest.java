package cc.allio.turbo.common.event;

import cc.allio.turbo.common.aop.Aspects;
import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SubscribersTest extends BaseTestCase {

    private final DomainEventBus eventBus = new DomainEventBus();
    private final TurboAdvisor advisor = new BehaviorAdvisor(eventBus);

    @Test
    void testSubscribe() {

        CreditCard creditCard = Aspects.create(new CreditCard(eventBus, 100.0), advisor);
        Subscribers.from(creditCard)
                .subscribeOn(CreditCard::pay)
                .observe(subscription -> {
                    Double balance = subscription.getBehaviorResult(Double.class).orElseThrow(NullPointerException::new);
                    assertEquals(90.0, balance);
                });

        creditCard.pay(10.0);
    }
}
