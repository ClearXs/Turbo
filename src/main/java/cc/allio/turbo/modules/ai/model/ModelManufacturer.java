package cc.allio.turbo.modules.ai.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelManufacturer {

    OPENAI("openai"),
    ANTHROPIC("anthropic"),
    VERTEX("vertex"),
    OLLAMA("ollama"),
    MONOSHOT("monoshot"),
    QIAN_FAN("qianfan"),
    ZHIPU("zhipu");

    private final String name;
}
