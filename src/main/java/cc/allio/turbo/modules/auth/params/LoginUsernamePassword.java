package cc.allio.turbo.modules.auth.params;

import lombok.Data;

/**
 * corresponding {@link LoginMode#EMPTY} login method
 */
@Data
public class LoginUsernamePassword implements LoginClaim {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * login mode
     */
    private LoginMode loginMode;
}
