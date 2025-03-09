package cc.allio.turbo.modules.ai.api.vo;

import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.entity.AIMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConversationVO extends AIChat {

    @Schema(description = "latest user message")
    private String latestUserMessage;

    @Schema(description = "all instructions")
    private List<AIMessage> messages;
}
