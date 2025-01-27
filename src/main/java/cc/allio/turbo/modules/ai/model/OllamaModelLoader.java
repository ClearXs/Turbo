package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;

public class OllamaModelLoader implements ModelLoader {
    @Override
    public ChatModel load(ModelOptions options, List<Tool> tools) {
        return null;
    }
}
