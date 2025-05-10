package cc.allio.turbo.common.web;

import cc.allio.turbo.common.jackson.CustomMappingJackson2httpMessageConverter;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableAspectJAutoProxy
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // support all cross-origin requests
        registry.addMapping("*");
    }

    /**
     * 默认的转换器由{@link RestTemplate}进行创建
     *
     * @see HttpMessageConverters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 添加自定义的converter，如果使用configureMessageConverters它的优先级高于高于默认的converter，
        // 导致二进制数据序列化产生问题（spring默认使用ByteArrayHttpMessageConverter），采取extendMessageConverters添加至转换器尾部
        converters.addLast(new CustomMappingJackson2httpMessageConverter(objectMapper));

        JsonUtils.reset(objectMapper);
    }

}
