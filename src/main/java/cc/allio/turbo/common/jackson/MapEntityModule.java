package cc.allio.turbo.common.jackson;

import cc.allio.turbo.common.db.entity.MapEntity;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapEntityModule extends SimpleModule {

    public MapEntityModule() {
        addSerializer(MapEntity.class, new MapEntitySerializer());
        addDeserializer(MapEntity.class, new MapEntityDeserializer());
    }
}
