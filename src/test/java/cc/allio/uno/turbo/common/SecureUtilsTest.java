package cc.allio.uno.turbo.common;

import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.turbo.common.constant.SecureAlgorithm;
import cc.allio.uno.turbo.common.util.SecureUtil;
import org.junit.jupiter.api.Test;

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
}
