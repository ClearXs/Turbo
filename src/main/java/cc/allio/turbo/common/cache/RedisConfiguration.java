package cc.allio.turbo.common.cache;

import cc.allio.turbo.common.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }

}
