package cc.allio.turbo.modules.ai.resources.prompt;

import cc.allio.turbo.modules.ai.exception.ResourceParseException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: acquire with persistent storage
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class PromptManager {

    private final Map<String, SelfPrompt> prompts;

    public PromptManager() {
        prompts = Maps.newConcurrentMap();
    }

    public void loadFromResources() throws ResourceParseException {
        AIResources resources = AIResources.getInstance().readNow();
        Collection<AIResources.LiteralAgent> allAgent = resources.getAllAgent();
        for (AIResources.LiteralAgent agent : allAgent) {
            String prompt = agent.getPrompt();
            prompts.put(agent.getName(), new SelfPrompt(prompt));
        }
    }

    public Optional<SelfPrompt> getByName(String name) {
        return Optional.of(prompts.get(name));
    }

}
