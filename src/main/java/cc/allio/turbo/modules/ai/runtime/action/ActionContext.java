package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.runtime.TaskContext;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChainContext;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Optional;
import java.util.Queue;

/**
 * the {@link Chain} context.
 *
 * @author j.x
 * @since 0.2.0
 */
public class ActionContext extends DefaultChainContext<TaskContext> {

    /**
     * maybe existing many output
     */
    private final Queue<Output> outputs = Queues.newConcurrentLinkedQueue();

    @Getter
    @Setter
    private ExecutionMode mode;

    public ActionContext(TaskContext taskContext) {
        super(taskContext);
    }

    /**
     * add {@link Output} in context
     *
     * @param output the {@link Output} instance
     */
    public void addOutput(@NonNull Output output) {
        outputs.add(output);
    }

    /**
     * take {@link Output} in context.
     *
     * @return the optional {@link Output}
     */
    public Optional<Output> takeOutput() {
        return Optional.ofNullable(outputs.poll());
    }

    /**
     * pop out first {@link Output}
     *
     * @return the optional {@link Output}
     */
    public Optional<Output> firstOutput() {
        return outputs.stream().findFirst();
    }
}
