package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.List;

public class OpenAIModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, List<Tool> tools) {

        OpenAiApi api = new OpenAiApi(options.getApiKey());

        OpenAiChatOptions.builder()
                .model(options.getModel());

        return null;
    }
}
