package cc.allio.turbo.modules.ai.resources.prompt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.chat.prompt.Prompt;

@Getter
@AllArgsConstructor
public class SelfPrompt {

    String content;

    public Prompt getRealPrompt() {
        return null;
    }
}
