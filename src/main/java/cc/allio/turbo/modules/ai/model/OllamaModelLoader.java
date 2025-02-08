package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.FunctionTool;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.util.Set;

/**
 * Ollama Model loader
 *
 * @author j.x
 * @since 0.2.0
 */
public class OllamaModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        OllamaApi ollamaApi;
        String address = options.getAddress();
        if (StringUtils.isNotEmpty(address)) {
            ollamaApi = new OllamaApi(address);
        } else {
            ollamaApi = new OllamaApi();
        }
        // @see https://github.com/ollama/ollama/blob/main/docs/modelfile.md#parameter
        OllamaOptions ollamaOptions =
                OllamaOptions.builder()
                        .temperature(options.getTemperature())
                        .model(options.getModel())
                        .topK(options.getTopK())
                        .topP(options.getTopP())
                        .presencePenalty(options.getPresencePenalty())
                        .stop(options.getStopSequences())
                        .frequencyPenalty(options.getFrequencyPenalty())
                        .mirostat(options.getOptional("mirostat", Integer.class).orElse(null))
                        .mirostatEta(options.getOptional("mirostatEta", Float.class).orElse(null))
                        .mirostatTau(options.getOptional("mirostatTau", Float.class).orElse(null))
                        .numCtx(options.getOptional("numCtx", Integer.class).orElse(null))
                        .repeatLastN(options.getOptional("repeatLastN", Integer.class).orElse(null))
                        .repeatPenalty(options.getOptional("repeatPenalty", Double.class).orElse(null))
                        .seed(options.getOptional("seed", Integer.class).orElse(null))
                        .build();
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(ollamaOptions)
                .build();
    }
}
