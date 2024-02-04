package cc.allio.turbo.common.jackson;

import cc.allio.uno.data.orm.dsl.DSLName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DSLSerializer extends com.fasterxml.jackson.databind.ser.std.StdScalarSerializer<DSLName> {

    public DSLSerializer() {
        super(DSLName.class);
    }

    @Override
    public void serialize(DSLName value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.format());
    }
}
