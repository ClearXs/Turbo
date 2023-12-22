package cc.allio.turbo.common.jackson;

import cc.allio.uno.core.util.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends com.fasterxml.jackson.databind.ser.std.DateSerializer {

    @Override
    public void serialize(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
        g.writeString(DateUtil.format(value, DateUtil.PATTERN_DATETIME));
    }
}
