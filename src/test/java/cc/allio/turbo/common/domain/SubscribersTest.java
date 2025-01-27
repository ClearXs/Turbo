package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.aop.Aspects;
import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SubscribersTest extends BaseTestCase {

    private final TurboAdvisor advisor = new BehaviorAdvisor(EventBusFactory.current());

    @Test
    void testSubscribe() {
        CreditCard creditCard = Aspects.create(new CreditCard(EventBusFactory.current(), 100.0), advisor);

        DomainTools<Double> tools = DomainTools.fromActual(creditCard);
        tools.subscribeOn(CreditCard::pay)
                .observe(subscription -> {
                    Double balance = subscription.getBehaviorResult(Double.class).orElseThrow(NullPointerException::new);
                    assertEquals(90.0, balance);
                });

        tools.watch(CreditCard::pay)
                .untilComplete()
                .whenTrigger(() -> creditCard.pay(10.0))
                .executable(() -> {
                    assertTrue(1 == 1);
                })
                .doOn();
    }
}
