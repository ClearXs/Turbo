package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.Set;

public class QianfanModelLoader implements ModelLoader {
    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        return null;
    }
}
