package cc.allio.turbo.common.jackson;

import cc.allio.turbo.common.api.Key;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;

/**
 * {@link Key} deserializer
 *
 * @author j.x
 * @date 2024/7/17 18:17
 * @since 0.1.1
 */
public class KeyDeserializer extends StdScalarDeserializer<Key> {

    public KeyDeserializer() {
        super(Key.class);
    }

    @Override
    public Key deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String str = p.getText().trim();
            return new Key(str);
        }
        return null;
    }

}
