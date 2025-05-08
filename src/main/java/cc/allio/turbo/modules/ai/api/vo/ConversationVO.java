package cc.allio.turbo.modules.ai.api.vo;

import cc.allio.turbo.modules.ai.entity.AIChat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConversationVO extends AIChat {

    @Schema(description = "latest user message")
    private String latestUserMessage;

}
