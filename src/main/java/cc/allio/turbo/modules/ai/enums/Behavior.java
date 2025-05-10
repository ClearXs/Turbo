package cc.allio.turbo.modules.ai.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Behavior {

    FUNCTION("function", "function"),
    LLM("llm", "llm"),
    PROMPT("prompt", "prompt");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
