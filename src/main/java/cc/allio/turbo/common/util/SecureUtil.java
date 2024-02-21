package cc.allio.turbo.common.util;

import cc.allio.turbo.common.constant.SecureAlgorithm;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.StringUtils;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * 安全相关工具类
 *
 * @author j.x
 * @date 2023/10/23 11:40
 * @since 0.1.0
 */
@Slf4j
public class SecureUtil {

    /**
     * 数字和26个字母组成
     */
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * PBE and MD5 and DES
     */
    private static final String PBE_ALGORITHM = "PBEWithMD5andDES";
    private static final String AES_ALGORITHM = "AES";
    private static final PBECipher pbeCipher = new PBECipher();
    private static final AESCipher aesCipher = new AESCipher();

    /**
     * 获取当前系统使用的密钥
     */
    public static String getSystemSecretKey() {
        return Envs.getProperty("secure.secret-key");
    }

    /**
     * 获取当前系统使用的加密算法，如果系统没有内置则使用AES作为默认
     */
    public static SecureCipher getSystemSecureCipher() {
        String algorithm = Envs.getProperty("secure.secure-algorithm");
        if (StringUtils.isBlank(algorithm)) {
            return aesCipher;
        } else {
            SecureAlgorithm secureAlgorithm = SecureAlgorithm.getAlgorithm(algorithm);
            if (secureAlgorithm == null) {
                return aesCipher;
            } else {
                return getSecureCipher(secureAlgorithm);
            }
        }
    }

    public static SecureCipher getSecureCipher(SecureAlgorithm algorithm) {
        if (SecureAlgorithm.PBE == algorithm) {
            return pbeCipher;
        }
        return aesCipher;
    }

    public static class AESCipher implements SecureCipher {

        private final String transformation;

        public AESCipher() {
            this("AES/ECB/PKCS5Padding");
        }

        public AESCipher(String transformation) {
            this.transformation = transformation;
        }

        @Override
        public String encrypt(String plainText, String secretKeyText, String salt) {
            Cipher cipher = getCipher(secretKeyText, Cipher.ENCRYPT_MODE);
            if (cipher == null) {
                return StringPool.EMPTY;
            }
            try {
                byte[] secretBytes = cipher.doFinal(plainText.getBytes());
                return new String(Base64.getEncoder().encode(secretBytes), StandardCharsets.UTF_8);
            } catch (Throwable ex) {
                return StringPool.EMPTY;
            }
        }

        @Override
        public String decrypt(String cipherText, String secretKeyText, String salt) {
            Cipher cipher = getCipher(secretKeyText, Cipher.DECRYPT_MODE);
            if (cipher == null) {
                return StringPool.EMPTY;
            }
            try {
                byte[] decode = Base64.getDecoder().decode(cipherText.getBytes());
                return new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
            } catch (Throwable ex) {
                return StringPool.EMPTY;
            }
        }

        /**
         * 获取cipher 实例
         *
         * @param mode cipher mode
         */
        private Cipher getCipher(String secretKey, int mode) {
            Cipher cipher = null;
            try {
                // "算法/模式/补码方式（填充方式）"
                cipher = Cipher.getInstance(this.transformation);
                SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);
                cipher.init(mode, keySpec);
            } catch (Throwable ex) {
                log.error("from secureKey {} get AES Cipher instance failed", secretKey, ex);
            }
            return cipher;
        }
    }

    public static class PBECipher implements SecureCipher {

        @Override
        public String encrypt(String plainText, String secretKeyText, String salt) {
            Cipher cipher = getCipher(secretKeyText, salt, Cipher.ENCRYPT_MODE);
            try {
                byte[] secureBytes = cipher.doFinal(plainText.getBytes());
                return new String(Base64.getEncoder().encode(secureBytes), StandardCharsets.UTF_8);
            } catch (Throwable ex) {
                return StringPool.EMPTY;
            }
        }

        @Override
        public String decrypt(String cipherText, String secretKeyText, String salt) {
            Cipher cipher = getCipher(secretKeyText, salt, Cipher.DECRYPT_MODE);
            byte[] decode = Base64.getDecoder().decode(cipherText);
            try {
                return new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
            } catch (Throwable ex) {
                return StringPool.EMPTY;
            }
        }

        /**
         * 获取PBE的{@link Cipher}对象
         *
         * @param secureKeyText 密钥，该密钥位数必须是16、24、32
         * @param salt          研制
         * @param cipherMode    模式
         * @return Cipher instance or null
         */
        private Cipher getCipher(String secureKeyText, String salt, int cipherMode) {
            // 密钥
            PBEKeySpec keySpec = new PBEKeySpec(secureKeyText.toCharArray());
            // 盐值
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), 1000);
            Cipher cipher = null;
            try {
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                cipher = Cipher.getInstance(PBE_ALGORITHM);
                cipher.init(cipherMode, secretKey, parameterSpec);
            } catch (Throwable ex) {
                // ignore
                log.error("from secureKey {}, salt {} get PBE Cipher instance failed", secureKeyText, salt, ex);
            }
            return cipher;
        }
    }

    public interface SecureCipher {

        /**
         * <p>加密步骤:</p>
         * <ol>
         *  <li>明文</li>
         *  <li>PBE加密</li>
         *  <li>Base64加密</li>
         *  <li>密文</li>
         * </ol>
         *
         * @param plainText 明文
         * @param salt      盐值
         * @return 密文 or empty
         */
        String encrypt(String plainText, String secretKeyText, String salt);

        /**
         * <p>解密步骤</p>
         * <ol>
         *     <li>密文</li>
         *     <li>Base64解密</li>
         *     <li>PBE解密</li>
         *     <li>明文</li>
         * </ol>
         *
         * @param cipherText 密文
         * @param salt       盐值
         * @return 明文 or empty
         */
        String decrypt(String cipherText, String secretKeyText, String salt);

    }

    /**
     * 获取指定长度的随机的安全的key
     *
     * @param length 长度
     * @return secure key
     */
    public static String getRandomSecureKey(int length) {
        char[] nonceChars = new char[length];  //指定长度为6位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(secureRandom.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 生成RSA公钥、私钥
     *
     * @return KeyPair instance
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyRSAPair() throws NoSuchAlgorithmException {
        // 生成RSA公钥、私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static RSAPublicKey publicKey;
    private static RSAPrivateKey privateKey;

    // 预先从文件存储中获取配置在项目的公钥与私钥
    static {
        ClassLoader classLoader = SecureUtil.class.getClassLoader();
        // 尝试从/resources/rsa目录下获取公私钥文件，如果出错则采取默认生成的公钥私钥
        try {
            // private key
            URL resource = classLoader.getResource("rsa/private.pem");
            if (resource == null) {
                throw new NullPointerException("rsa/private.pem file not find");
            }
            String privateKeyString = IoUtils.readToString(new FileInputStream(resource.getFile()));
            RSAKey rsaKey = (RSAKey) JWK.parseFromPEMEncodedObjects(privateKeyString);
            privateKey = rsaKey.toRSAPrivateKey();

            // public key
            resource = classLoader.getResource("rsa/public.pem");
            if (resource == null) {
                throw new NullPointerException("rsa/public.pem file not find");
            }
            String publicKeyString = IoUtils.readToString(new FileInputStream(resource.getFile()));
            rsaKey = (RSAKey) JWK.parseFromPEMEncodedObjects(publicKeyString);
            publicKey = rsaKey.toRSAPublicKey();
        } catch (Throwable ex) {
            log.error("generate rsa catalog public and private key failed, used default generate key pair create public private key");
            try {
                KeyPair keyPair = generateKeyRSAPair();
                publicKey = (RSAPublicKey) keyPair.getPublic();
                privateKey = (RSAPrivateKey) keyPair.getPrivate();
            } catch (NoSuchAlgorithmException e) {
                // ignore
            }
        }
    }

    /**
     * 获取存在于/resources/rsa目录下的公钥。其名称必须是public.pem
     *
     * @return RSAPublicKey or null
     */
    public static RSAPublicKey getSystemRasPublicKey() {
        return publicKey;
    }


    /**
     * 获取存在于/resources/rsa目录下的公钥。其名称必须是public.pem
     *
     * @return RSAPublicKey or null
     */
    public static RSAPrivateKey getSystemRasPrivateKey() {
        return privateKey;
    }
}
