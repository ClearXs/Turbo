package cc.allio.turbo.modules.auth.params;

/**
 * support login parameters
 *
 * @author j.x
 * @date 2024/4/6 17:36
 * @since 0.1.1
 */
public interface LoginClaim {

    /**
     * get login mode
     *
     * @return the login mode
     */
    LoginMode getLoginMode();
}
