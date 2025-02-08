package cc.allio.turbo.modules.ai.model;

import cc.allio.turbo.common.db.entity.MapEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.ai.chat.prompt.ChatOptions;

import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModelOptions extends MapEntity implements ChatOptions {

    private ModelManufacturer manufacturer;

    // model api address or accessible address.
    // like as http://ollama:11434
    private String address;
    // model name
    private String model;

    // access key
    private String apiKey;
    // access secret key (optional)
    private String secretKey;
    private Double frequencyPenalty;
    private Integer maxTokens;
    private Double presencePenalty;
    private List<String> stopSequences;
    private Double temperature;
    private Integer topK;
    private Double topP;

    /**
     * get value in map and return {@link java.util.Optional}
     *
     * @param key       the map key
     * @param valueType the value type class
     * @param <V>       value type
     * @return
     */
    public <V> Optional<V> getOptional(String key, Class<V> valueType) {
        return Optional.ofNullable(get(key)).map(valueType::cast);
    }

    @Override
    public <T extends ChatOptions> T copy() {
        throw new UnsupportedOperationException();
    }
}
