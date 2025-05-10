package cc.allio.turbo.modules.development.configuration;

import cc.allio.turbo.modules.development.jackson.DevelopMappingJackson2httpMessageConverter;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableAspectJAutoProxy
public class DevelopWebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.addLast(new DevelopMappingJackson2httpMessageConverter(objectMapper));

        JsonUtils.reset(objectMapper);
    }
}
