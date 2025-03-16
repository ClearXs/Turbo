package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.aop.Aspects;
import cc.allio.turbo.common.aop.TurboAdvisor;
import cc.allio.turbo.common.aop.TurboAspectConfiguration;
import cc.allio.turbo.common.db.entity.Org;
import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@RunTest(components = {
        DomainEventConfiguration.class,
        OrgServiceImpl.class,
        TurboAspectConfiguration.class})
public class SubscriberTest extends BaseTestCase {

    @Inject
    private OrgServiceImpl orgService;

    private final TurboAdvisor advisor = new BehaviorAdvisor(EventBusFactory.current());

    @Test
    void testSubscribe() {
        CreditCard creditCard = Aspects.create(new CreditCard(EventBusFactory.current(), 100.0), advisor);

        DomainTools<Double> tools = DomainTools.fromActual(creditCard);
        tools.subscribeOn(CreditCard::pay)
                .observeOnConsummation(subscription -> {
                    if (subscription instanceof BehaviorSubscription<?> behaviorSubscription) {
                        Double balance = behaviorSubscription.getBehaviorResult(Double.class).orElseThrow(NullPointerException::new);
                        assertEquals(90.0, balance);
                    }
                });

        tools.watch(CreditCard::pay)
                .untilComplete()
                .whenTrigger(() -> creditCard.pay(10.0))
                .executable(() -> {
                    assertTrue(1 == 1);
                })
                .doOn();
    }

    @Test
    public void testEmptyParameterMethod() {
        orgService.subscribeOn(orgService::getName)
                .observeOnConsummation(subscription -> {
                    Optional<Object> behaviorResult = Optional.empty();
                    if (subscription instanceof BehaviorSubscription<Org> behaviorSubscription) {
                        behaviorResult = behaviorSubscription.getBehaviorResult();
                    }
                    boolean present = behaviorResult.isPresent();
                    assertTrue(present);

                    Object o = behaviorResult.get();
                    assertEquals("org", o);
                });

        orgService.getName();
    }

    @Test
    void testBeanInitializationPublish() {
    }
}
