package cc.allio.turbo.modules.ai.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {

    LOADING("loading", "loading"),
    COMPLETE("complete", "complete"),
    INCOMPLETE("incomplete,", "incomplete"),
    ERROR("error", "error");

    @JsonValue
    private final String value;
    private final String label;
}
