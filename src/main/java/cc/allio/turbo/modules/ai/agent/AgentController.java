package cc.allio.turbo.modules.ai.agent;

import cc.allio.turbo.common.event.DomainEventBus;
import cc.allio.turbo.modules.ai.exception.ResourceParseException;
import cc.allio.turbo.modules.ai.resources.AIResources;
import cc.allio.turbo.modules.ai.resources.prompt.PromptManager;
import cc.allio.turbo.modules.ai.task.action.ActionRegistry;
import cc.allio.turbo.modules.ai.task.action.LocalActionRegistry;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * control several agents.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public class AgentController {

    final PromptManager promptManager;
    final DomainEventBus eventBus;
    final Map<String, Agent> agentRegistry;

    final ActionRegistry actionRegistry;
    final AIResources resources;

    public AgentController(DomainEventBus eventBus) {
        this.promptManager = new PromptManager();
        this.eventBus = eventBus;
        this.agentRegistry = Maps.newConcurrentMap();
        this.resources = AIResources.getInstance();
        this.actionRegistry = new LocalActionRegistry(resources);
    }

    /**
     * initialization agent system
     */
    void setup() throws ResourceParseException {

        resources.readNow();
        // load resource
        try {
            promptManager.loadFromResources();
        } catch (ResourceParseException e) {
            log.warn("Failed to load prompt from resources", e);
        }

        // load all agent
        SMAAgent smaAgent = new SMAAgent(resources, actionRegistry);
        agentRegistry.put(smaAgent.getName(), smaAgent);

        // subscribe user input


    }



}
