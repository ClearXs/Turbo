package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;

import java.util.List;

/**
 * use by {@link ZhiPuAiChatModel}
 *
 * @author j.x
 * @since 0.2.0
 */
public class ZhipuModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, List<Tool> tools) {
        ZhiPuAiApi api = new ZhiPuAiApi(options.getApiKey());
        ZhiPuAiChatOptions zhiPuAiChatOptions =
                ZhiPuAiChatOptions.builder()
                        .model(options.getModel())
                        .doSample(options.getOptional("doSample", Boolean.class).orElse(null))
                        .maxTokens(options.getMaxTokens())
                        .stop(options.getStopSequences())
                        .temperature(options.getTemperature())
                        .topP(options.getTopP())
                        .build();
        return new ZhiPuAiChatModel(api, zhiPuAiChatOptions);
    }
}
