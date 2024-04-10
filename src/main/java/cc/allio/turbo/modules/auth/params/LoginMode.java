package cc.allio.turbo.modules.auth.params;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginMode {

    EMPTY("EMPTY", "无额外方式"),
    CAPTCHA_CODE("CAPTCHA_CODE", "验证码"),
    VERIFICATION_CODE("VERIFICATION_CODE", "短信验证码");

    @JsonValue
    private final String value;
    private final String label;

    public static LoginMode of(String value) {
        for (LoginMode loginMode : values()) {
            if (loginMode.value.equals(value)) {
                return loginMode;
            }
        }
        return null;
    }
}
