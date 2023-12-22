package cc.allio.turbo.common.constant;

/**
 * 定义系统可用的加密算法
 *
 * @author j.x
 * @date 2023/10/23 17:52
 * @since 0.1.0
 */
public enum SecureAlgorithm {

    AES,
    PBE;

    public static SecureAlgorithm getAlgorithm(String algorithm) {
        for (SecureAlgorithm v : values()) {
            if (v.name().equalsIgnoreCase(algorithm)) {
                return v;
            }
        }
        return null;
    }
}
