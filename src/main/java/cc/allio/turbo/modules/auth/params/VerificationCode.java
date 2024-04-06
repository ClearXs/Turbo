package cc.allio.turbo.modules.auth.params;

import lombok.Data;

@Data
public class VerificationCode {

    /**
     * 短信验证码
     */
    private String code;
}
