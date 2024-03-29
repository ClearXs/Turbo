package cc.allio.turbo.modules.message.runtime;

import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.convert.ConverterFactory;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingField;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * 消息接收实体
 *
 * @author j.x
 * @date 2024/3/29 00:24
 * @since 0.1.1
 */
@Data
public class ReceiveMetadata implements Metadata {

    private String configKey;

    private String businessConfigKey;
    private Map<String, Object> variables;
    private final MappingMetadata metadata;

    public ReceiveMetadata() {
        this.metadata = new DefaultMappingMetadata(ConverterFactory.createConverter(this.getClass()));
        this.variables = Maps.newHashMap();
        this.metadata.addMapping(MappingField.builder().name("configKey").build(), MappingField.builder().name("configKey").build());
        this.metadata.addMapping(MappingField.builder().name("businessConfigKey").build(), MappingField.builder().name("businessConfigKey").build());
    }

    @Override
    public MappingMetadata getMapping() {
        return metadata;
    }

    @Override
    public Map<String, Object> getValues() {
        return variables;
    }

    public Map<String, Object> getVariables() {
        return getValues();
    }
}
