package cc.allio.turbo.modules.ai.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Capability {

    CHAT("chat", "Chat"),
    EMBEDDING("embedding", "Embedding"),
    FUNCTION("function", "Function"),
    VISUAL("visual", "Visual"),
    AUDIO("audio", "Audio"),
    DEEP_THINK("deep_thing", "Deep Thing");

    private final String value;
    private final String label;
}
