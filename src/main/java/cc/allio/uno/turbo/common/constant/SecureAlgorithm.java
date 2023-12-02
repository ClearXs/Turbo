package cc.allio.uno.turbo.common.constant;

/**
 * 定义系统可用的加密算法
 *
 * @author j.x
 * @date 2023/10/23 17:52
 * @since 1.0.0
 */
public enum SecureAlgorithm {

    AES,
    PBE;

    public static SecureAlgorithm getAlgorithm(String algorithm) {
        return valueOf(algorithm);
    }
}
