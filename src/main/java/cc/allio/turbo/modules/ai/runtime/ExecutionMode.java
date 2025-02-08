package cc.allio.turbo.modules.ai.runtime;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutionMode {

    STREAM("stream", "stream"),
    CALL("call", "call");

    private final String name;
    @JsonValue
    private final String value;
}
