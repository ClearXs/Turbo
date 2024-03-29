package cc.allio.turbo.modules.message.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Target {

    ORG("ORG", "组织"),
    POST("POST", "岗位"),
    ROLE("ROLE", "角色"),
    USER("USER", "用户"),
    CUSTOM("CUSTOM", "自定义"),
    ALL_USER("ALL_USER", "所有用户");

    @JsonValue
    private final String value;
    private final String label;
}
