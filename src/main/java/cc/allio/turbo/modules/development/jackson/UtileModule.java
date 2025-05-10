package cc.allio.turbo.modules.development.jackson;

import cc.allio.turbo.modules.development.api.Key;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * some utility serializable and deserializable
 *
 * @author j.x
 * @date 2024/7/17 18:08
 * @since 0.1.1
 */
public class UtileModule extends SimpleModule {

    public UtileModule() {
        addSerializer(Key.class, new KeySerializer());
        addDeserializer(Key.class, new KeyDeserializer());
    }

}
