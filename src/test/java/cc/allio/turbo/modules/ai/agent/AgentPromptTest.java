package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.modules.ai.agent.runtime.Environment;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class AgentPromptTest extends BaseTestCase {


    static final String SYSTEM_PROMPT_TEMPLATE = "Your are is proficient translator, you should be able to translate the following text from {origin_language} to {target_language}.";

    static final String USER_MESSAGE_1 = "Translate {word1}";

    static final String USER_MESSAGE_2 = "Translate {word2}";

    @Test
    void testCorrectnessForText() {
        Environment environment = new Environment();
        AgentPrompt prompt =
                AgentPrompt.from(SYSTEM_PROMPT_TEMPLATE, environment);

        environment.put("origin_language", "English");
        environment.put("target_language", "Chinese");

        environment.put("word1", "hello");
        environment.put("word2", "world");

        List<Message> instructions = prompt.getInstructions();

        assertEquals(1, instructions.size());

        assertEquals("Your are is proficient translator, you should be able to translate the following text from English to Chinese.", instructions.getFirst().getText());

    }

}
