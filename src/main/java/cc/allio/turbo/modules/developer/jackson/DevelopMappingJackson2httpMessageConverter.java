package cc.allio.turbo.modules.developer.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * develop jackson convert
 *
 * @author j.x
 * @date 2024/8/14 10:18
 * @since 0.1.1
 */
public class DevelopMappingJackson2httpMessageConverter extends MappingJackson2HttpMessageConverter {

    public DevelopMappingJackson2httpMessageConverter(ObjectMapper objectMapper) {
        super(additionModule(objectMapper));
    }

    public static ObjectMapper additionModule(ObjectMapper objectMapper) {
        objectMapper.registerModule(new UtileModule());
        return objectMapper;
    }
}
