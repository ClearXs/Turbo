package cc.allio.turbo.modules.ai;

import cc.allio.turbo.modules.ai.agent.Supervisor;
import cc.allio.turbo.modules.ai.evaluation.EvaluationController;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

@RunTest(components = AIConfiguration.class)
public class AIConfigurationTest extends BaseTestCase {

    @Inject
    private Supervisor agentController;

    @Inject
    private EvaluationController evaluationController;

    @Test
    void testSuccessForInjection() {
        assertNotNull(agentController);
        assertNotNull(evaluationController);
    }
}
