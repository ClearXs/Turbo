package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.chat.tool.FunctionTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;

import java.util.List;
import java.util.Set;

/**
 * use by {@link ZhiPuAiChatModel}
 *
 * @author j.x
 * @since 0.2.0
 */
public class ZhipuModelLoader implements ModelLoader {

    @Override
    public ChatModel load(ModelOptions options, Set<FunctionTool> tools) {
        ZhiPuAiApi api = new ZhiPuAiApi(options.getApiKey());

        // build tools
        List<ZhiPuAiApi.FunctionTool> zhipuTools =
                tools.stream()
                        .map(tool -> {
                            ZhiPuAiApi.FunctionTool.Function function =
                                    new ZhiPuAiApi.FunctionTool.Function(tool.name(), tool.description(), tool.parameters());
                            return new ZhiPuAiApi.FunctionTool(ZhiPuAiApi.FunctionTool.Type.FUNCTION, function);
                        })
                        .toList();

        ZhiPuAiChatOptions zhiPuAiChatOptions =
                ZhiPuAiChatOptions.builder()
                        .model(options.getModel())
                        .doSample(options.getOptional("doSample", Boolean.class).orElse(null))
                        .maxTokens(options.getMaxTokens())
                        .stop(options.getStopSequences())
                        .temperature(options.getTemperature())
                        .topP(options.getTopP())
                        .tools(zhipuTools)
                        .build();
        return new ZhiPuAiChatModel(api, zhiPuAiChatOptions);
    }
}
