package cc.allio.turbo.extension.swift;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class SwiftAutoConfiguration {

    @Bean
    public Swift swift(StringRedisTemplate redisTemplate) {
        return new Swift(redisTemplate);
    }
}
