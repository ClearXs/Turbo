package cc.allio.turbo.modules.developer.jackson;

import cc.allio.turbo.modules.developer.api.Key;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * {@link Key} {@link StdSerializer}
 *
 * @author j.x
 * @date 2024/7/17 18:10
 * @since 0.1.1
 */
public class KeySerializer extends com.fasterxml.jackson.databind.ser.std.StdScalarSerializer<Key> {

    public KeySerializer() {
        super(Key.class);
    }

    @Override
    public void serialize(Key value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.get());
    }
}
