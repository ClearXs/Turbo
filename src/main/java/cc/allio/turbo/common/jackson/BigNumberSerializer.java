package cc.allio.turbo.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

public class BigNumberSerializer extends NumberSerializer {

    /**
     * @param rawType
     * @since 2.5
     */
    public BigNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    @Override
    public void serialize(Number value, JsonGenerator g, SerializerProvider provider) throws IOException {
        long longValue = value.longValue();
        if (longValue >= (long) Integer.MAX_VALUE) {
            g.writeString(String.valueOf(longValue));
        } else {
            g.writeNumber(longValue);
        }
    }
}
