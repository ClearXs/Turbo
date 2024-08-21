package cc.allio.turbo.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 增强http jackson序列化处理
 *
 * @author j.x
 * @date 2023/11/14 22:18
 * @since 0.1.0
 */
public class CustomMappingJackson2httpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomMappingJackson2httpMessageConverter(ObjectMapper objectMapper) {
        super(initCustomJacksonMapper(objectMapper));
    }

    public static ObjectMapper initCustomJacksonMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new BigNumberModule());
        objectMapper.registerModule(new DateModule());
        objectMapper.registerModule(new DSLNameModule());
        objectMapper.registerModule(new MapEntityModule());
        return objectMapper;
    }
}
