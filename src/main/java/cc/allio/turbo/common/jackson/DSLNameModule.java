package cc.allio.turbo.common.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class DSLNameModule extends SimpleModule {

    public DSLNameModule() {
        addSerializer(new DSLSerializer());
    }
}
