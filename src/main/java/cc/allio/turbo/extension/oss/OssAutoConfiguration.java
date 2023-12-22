package cc.allio.turbo.extension.oss;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssAutoConfiguration {

    @Bean
    public OssUpdateListener ossUpdateListener() {
        return new OssUpdateListener();
    }
}
