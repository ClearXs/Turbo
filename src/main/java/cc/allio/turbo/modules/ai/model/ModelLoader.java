package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.modules.ai.runtime.tool.Tool;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;

/**
 * spring AI model loader
 *
 * @author j.x
 * @since 0.2.0
 */
public interface ModelLoader {

    ChatModel load(ModelOptions options, List<Tool> tools);
}
