package cc.allio.turbo.modules.message.runtime;

import cc.allio.uno.core.metadata.convert.Converter;
import cc.allio.uno.core.metadata.convert.ConverterFactory;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 消息源转换器
 *
 * @author j.x
 * @date 2024/3/29 00:24
 * @since 0.1.1
 */
public class MessageSourceConverter implements SourceConverter<ReceiveMetadata> {

    @Override
    public JsonNode prepare(ApplicationContext context, JsonNode value) throws Throwable {
        return value;
    }

    @Override
    public ReceiveMetadata doConvert(ApplicationContext context, JsonNode value) throws Throwable {
        Converter<ReceiveMetadata> converter = ConverterFactory.createConverter(ReceiveMetadata.class);
        ReceiveMetadata receive = converter.execute(context, value);
        Map<String, Object> variables = JsonUtils.readMap(value.toPrettyString(), String.class, Object.class);
        receive.setVariables(variables);
        return receive;
    }
}
