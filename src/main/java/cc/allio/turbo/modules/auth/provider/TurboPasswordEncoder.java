package cc.allio.turbo.modules.auth.provider;

import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.turbo.common.util.SecureUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 默认使用aec加密
 *
 * @author j.x
 * @date 2023/10/23 17:51
 * @since 0.1.0
 */
@AllArgsConstructor
public class TurboPasswordEncoder implements PasswordEncoder {

    private final SecureProperties secureProperties;

    @Override
    public String encode(CharSequence rawPassword) {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        return secureCipher.encrypt(rawPassword.toString(), secureProperties.getSecretKey(), null);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        String decrypt = secureCipher.decrypt(encodedPassword, secureProperties.getSecretKey(), null);
        if (StringUtils.isBlank(decrypt)) {
            throw new BadCredentialsException(String.format("password %s decrypt inputStream empty", rawPassword));
        }
        return rawPassword.toString().equals(decrypt);
    }
}
