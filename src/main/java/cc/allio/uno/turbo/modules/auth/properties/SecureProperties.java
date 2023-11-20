package cc.allio.uno.turbo.modules.auth.properties;

import cc.allio.uno.turbo.modules.auth.constant.ExpireAt;
import cc.allio.uno.turbo.common.constant.SecureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "secure")
public class SecureProperties {

    /**
     * 加解密密钥
     */
    private String secretKey;

    /**
     * 忽略安全验证的path列表，支持通配符
     */
    private List<String> skipList;

    /**
     * 加密算法
     */
    private SecureAlgorithm secureAlgorithm;

    /**
     * jwt配置
     */
    private JWT jwt;

    /**
     * 验证码
     */
    private Captcha captcha;

    @Data
    public static class JWT {

        /**
         * jwt 发行人
         */
        private String issuer = "https://allio.cc";

        /**
         * token
         */
        private String subject = "token";

        /**
         * 默认一个小时
         */
        private ExpireAt expireAt = ExpireAt.H_1;
    }

    @Data
    public static class Captcha {

        /**
         * 验证码宽
         */
        private int width = 130;

        /**
         * 验证码高
         */
        private int height = 40;

        /**
         * 验证码长度
         */
        private int length = 4;

        /**
         * 过期时间
         */
        private ExpireAt expireAt;

    }

}
