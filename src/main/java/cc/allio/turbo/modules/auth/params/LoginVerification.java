package cc.allio.turbo.modules.auth.params;

import lombok.Data;

/**
 * corresponding {@link LoginMode#VERIFICATION_CODE} login method
 *
 * @author j.x
 * @date 2024/4/6 17:30
 * @since 0.1.1
 */
@Data
public class LoginVerification implements LoginClaim {

    /**
     * the phone number
     */
    private String phone;

    /**
     * login mode
     */
    private LoginMode loginMode;

    /**
     * the verification code
     */
    private VerificationCode verificationCode;
}
