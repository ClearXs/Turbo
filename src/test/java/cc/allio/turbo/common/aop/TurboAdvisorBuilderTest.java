package cc.allio.turbo.common.aop;

import cc.allio.turbo.common.domain.BehaviorAdvisor;
import cc.allio.uno.core.bus.EventBusFactory;
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
        BehaviorAdvisor behaviorAdvisor = BehaviorAdvisor.BehaviorAdvisorBuilder.builder(EventBusFactory.current())
                .build();
        assertNotNull(behaviorAdvisor);
    }

    public static class DirectTurboAdvisor extends TurboAdvisor {

    }
}
