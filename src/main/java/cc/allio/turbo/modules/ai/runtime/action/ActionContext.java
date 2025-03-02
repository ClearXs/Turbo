package cc.allio.turbo.modules.ai.runtime.action;

import cc.allio.turbo.modules.ai.Output;
import cc.allio.turbo.modules.ai.runtime.Environment;
import cc.allio.turbo.modules.ai.runtime.ExecutionMode;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.DefaultChainContext;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.Queue;

/**
 * the {@link Chain} context.
 *
 * @author j.x
 * @since 0.2.0
 */
public class ActionContext extends DefaultChainContext<Environment> {

    /**
     * maybe existing many output
     */
    private final Queue<Output> outputs = Queues.newConcurrentLinkedQueue();

    @Getter
    private final ExecutionMode mode;

    public ActionContext(Environment taskContext, ExecutionMode mode) {
        super(taskContext);
        this.mode = mode;
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
