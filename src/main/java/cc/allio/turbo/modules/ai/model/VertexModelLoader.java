package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Set;

public class VertexModelLoader implements ModelLoader {
    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        return null;
    }
}
