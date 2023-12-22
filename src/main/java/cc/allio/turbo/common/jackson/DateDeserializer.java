package cc.allio.turbo.common.jackson;

import cc.allio.uno.core.util.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends DateDeserializers.DateDeserializer {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String str = p.getText().trim();
            Date date;
            synchronized (this) {
                date = DateUtil.parse(str);
                if (date == null) {
                    return super.deserialize(p, ctxt);
                } else {
                    return date;
                }
            }
        }
        return super.deserialize(p, ctxt);
    }
}
