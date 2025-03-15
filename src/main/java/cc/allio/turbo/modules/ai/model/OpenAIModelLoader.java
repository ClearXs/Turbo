package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.List;
import java.util.Set;

/**
 * OPEN AI model loader
 *
 * @author j.x
 * @since 0.2.0
 */
public class OpenAIModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        OpenAiApi openAiApi = OpenAiApi.builder().baseUrl(options.getAddress()).apiKey(options.getApiKey()).build();
        // build tools
        List<OpenAiApi.FunctionTool> openAITools = tools.stream()
                .map(tool -> {
                    OpenAiApi.FunctionTool.Function function =
                            new OpenAiApi.FunctionTool.Function(tool.name(), tool.description(), tool.parameters(), false);
                    return new OpenAiApi.FunctionTool(OpenAiApi.FunctionTool.Type.FUNCTION, function);
                })
                .toList();

        // @see https://platform.openai.com/docs/api-reference/chat/create
        OpenAiChatOptions openAIOptions =
                OpenAiChatOptions.builder()
                        .temperature(options.getTemperature())
                        .topP(options.getTopP())
                        .frequencyPenalty(options.getFrequencyPenalty())
                        .maxTokens(options.getMaxTokens())
                        .presencePenalty(options.getPresencePenalty())
                        .stop(options.getStopSequences())
                        .maxCompletionTokens(options.getOptional("maxCompletionTokens", Integer.class).orElse(null))
                        .N(options.getOptional("n", Integer.class).orElse(null))
                        .seed(options.getOptional("seed", Integer.class).orElse(null))
                        .parallelToolCalls(options.getOptional("parallelToolCalls", Boolean.class).orElse(null))
                        .store(options.getOptional("store", Boolean.class).orElse(null))
                        .tools(openAITools)
                        .model(options.getModel())
                        .build();

        return OpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(openAIOptions).build();
    }
}
