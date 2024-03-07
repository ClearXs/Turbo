package cc.allio.turbo.common.util;

import cc.allio.uno.starter.core.UnoCoreAutoConfiguration;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.turbo.common.constant.SecureAlgorithm;
import cc.allio.turbo.common.util.SecureUtil;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

@RunTest(components = UnoCoreAutoConfiguration.class, active = "turbo")
public class SecureUtilsTest extends BaseTestCase {

    @Test
    void testAESDecrypt() {
        String randomSecureKey = SecureUtil.getRandomSecureKey(16);
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(SecureAlgorithm.AES);
        String plainText = "admin";
        String encrypt = secureCipher.encrypt("admin", randomSecureKey, null);

        String decrypt = secureCipher.decrypt(encrypt, randomSecureKey, null);
        assertEquals(plainText, decrypt);
    }

    @Test
    void testSystemSecurityKey() {
        String systemSecretKey = SecureUtil.getSystemSecretKey();

        assertEquals("efogaWfblI3TK5u1", systemSecretKey);
    }
}
