package cc.allio.turbo.modules.ai.agent.runtime;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutionMode {

    /**
     * chat output through streaming
     */
    STREAM("stream", "stream"),

    /**
     * directive output
     */
    CALL("call", "call");

    private final String name;
    @JsonValue
    private final String value;
}
