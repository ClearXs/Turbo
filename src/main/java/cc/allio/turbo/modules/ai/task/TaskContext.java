package cc.allio.turbo.modules.ai.task;

import lombok.Data;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;

@Data
public class TaskContext {

    private ChatModel chatModel;
    private Prompt prompt;
}
