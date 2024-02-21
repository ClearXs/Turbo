package cc.allio.turbo.common.aop;

import cc.allio.turbo.common.db.event.BehaviorAdvisor;
import cc.allio.turbo.common.db.event.DomainEventBus;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class TurboAdvisorBuilderTest extends BaseTestCase {

    @Test
    void testDirectCreateProxyAdvisor() {
        DirectTurboAdvisor directTurboAdvisor = TurboAdvisorBuilder.builder(DirectTurboAdvisor.class, beanClass -> true).build();
        assertNotNull(directTurboAdvisor);
        assertSame(DirectTurboAdvisor.class, directTurboAdvisor.getClass());
    }

    @Test
    void testBehaviorAdvisor() {
        BehaviorAdvisor behaviorAdvisor = BehaviorAdvisor.BehaviorAdvisorBuilder.builder(new DomainEventBus())
                .build();
        assertNotNull(behaviorAdvisor);
    }

    public static class DirectTurboAdvisor extends TurboAdvisor {

    }
}
