package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.aop.TurboAspectConfiguration;
import cc.allio.turbo.common.db.entity.Org;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@RunTest(components = {
        DomainEventConfiguration.class,
        OrgServiceImpl.class,
        TurboAspectConfiguration.class})
public class SubscriptionTest extends BaseTestCase {

    @Inject
    private OrgServiceImpl orgService;

    @Test
    public void testEmptyParameterMethod() throws InterruptedException {
        orgService.subscribeOn(orgService::getName)
                .observe(subscription -> {
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
}
