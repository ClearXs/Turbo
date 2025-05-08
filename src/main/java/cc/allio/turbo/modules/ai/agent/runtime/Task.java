package cc.allio.turbo.modules.ai.agent.runtime;

import cc.allio.turbo.common.domain.Observable;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.modules.ai.agent.runtime.action.builtin.EndAction;
import cc.allio.turbo.modules.ai.agent.runtime.action.builtin.StoreAction;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.agent.Agent;
import cc.allio.turbo.modules.ai.agent.runtime.action.builtin.ChatAction;
import cc.allio.turbo.modules.ai.chat.ChatService;
import cc.allio.turbo.modules.ai.model.AgentModel;
import cc.allio.turbo.modules.ai.agent.runtime.action.Action;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionContext;
import cc.allio.turbo.modules.ai.agent.runtime.action.ActionRegistry;
import cc.allio.turbo.modules.ai.store.ChatMessageStore;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * user task.
 * <p>
 * make plan
 *
 * @author j.x
 * @since 0.2.0
 */
public class Task {

    @Getter
    private final Long id;
    private final Agent formAgent;
    private final ActionRegistry actionRegistry;
    @Getter
    private final Environment environment;
    private final ChatMessageStore chatMessageStore;

    public Task(Agent formAgent,
                ActionRegistry actionRegistry,
                ChatMessageStore chatMessageStore) {
        this.id = IdGenerator.defaultGenerator().getNextId();
        this.formAgent = formAgent;
        this.actionRegistry = actionRegistry;
        this.environment = new Environment().injectOf(formAgent).injectOf(this);
        this.chatMessageStore = chatMessageStore;
    }

    /**
     * do execute current user task
     *
     * @param inputMono the input
     * @return the {@link Observable} for {@link Output}
     */
    public Observable<Output> execute(Mono<Input> inputMono) {
        return Observable.from(
                inputMono.flatMapMany(
                        input -> {
                            Environment newEnvironment = environment.copy();
                            if (input.getVariable() != null) {
                                newEnvironment.injectOf(input.getVariable());
                            }
                            AgentModel agentModel = new AgentModel(input.getModelOptions());
                            newEnvironment.setAgent(formAgent);
                            newEnvironment.setInput(input);
                            newEnvironment.setAgentModel(agentModel);
                            Chain<Environment, Output> planning = buildPlaning(agentModel, input, environment);
                            ActionContext actionContext = new ActionContext(newEnvironment, input.getExecutionMode());
                            return planning.processMany(actionContext);
                        })
        );
    }

    /**
     * specific task planing chain.
     */
    public Chain<Environment, Output> buildPlaning(AgentModel agentModel, Input input, Environment environment) {
        Set<String> dispatchActionNames = formAgent.getDispatchActionNames();
        List<Action> actions = Lists.newCopyOnWriteArrayList();
        // get action
        for (String actionName : dispatchActionNames) {
            // keep end action always last index.
            if (actionName.equals(Action.END)) {
                continue;
            }
            actionRegistry.getAction(actionName).ifPresent(actions::add);
        }

        // add default agent capability
        String conversationId = input.getConversationId();
        String sessionId = input.getSessionId();
        ChatMemory chatMemory =
                new EnvironmentPersistentSessionChatMemory(
                        AuthUtil.getAuthentication(),
                        sessionId,
                        chatMessageStore,
                        environment);
        ChatService chatService = new ChatService(agentModel, chatMemory, conversationId, sessionId);
        ChatAction chatAction = new ChatAction(chatService);
        actions.add(chatAction);

        // add store
        StoreAction storeAction = new StoreAction(chatMessageStore);
        actions.addFirst(storeAction);

        // add end
        EndAction endAction = new EndAction();
        actions.addLast(endAction);

        return new DefaultChain<>(actions);
    }
}
