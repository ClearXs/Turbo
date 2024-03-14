package cc.allio.turbo.common.jackson;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.uno.core.bean.BeanInfoWrapper;
import cc.allio.uno.core.type.Types;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于{@link java.beans.BeanInfo}把map数据转换为{@link MapEntity}
 *
 * @author j.x
 * @date 2024/2/3 00:00
 * @since 0.1.0
 */
public class MapEntityDeserializer extends StdDeserializer<MapEntity> implements ContextualDeserializer {

    private static final List<String> ignoreNames = List.of("empty", "class");
    private static final MapType mapType = TypeFactory.defaultInstance().constructMapType(MapEntity.class, String.class, Object.class);
    private final Class<? extends MapEntity> mapEntityClazz;

    public MapEntityDeserializer() {
        super(mapType);
        this.mapEntityClazz = null;
    }

    public MapEntityDeserializer(Class<? extends MapEntity> mapEntityClazz) {
        super(TypeFactory.defaultInstance().constructMapLikeType(mapEntityClazz, String.class, Object.class));
        this.mapEntityClazz = mapEntityClazz;
    }

    @Override
    public MapEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (mapEntityClazz == null) {
            throw new IllegalArgumentException("can't discern MapEntity class");
        }
        ValueInstantiator valueInstantiator = JDKValueInstantiators.findStdValueInstantiator(ctxt.getConfig(), HashMap.class);
        MapDeserializer deserialize = new MapDeserializer(mapType, valueInstantiator, null, new UntypedObjectDeserializer(null, mapType), null);
        Map<Object, Object> oriMap = deserialize.deserialize(p, ctxt);
        MapEntity mapEntity = cc.allio.uno.core.util.ClassUtils.newInstance(mapEntityClazz);
        for (Map.Entry<Object, Object> entry : oriMap.entrySet()) {
            mapEntity.put(Types.toString(entry.getKey()), entry.getValue());
        }

        BeanInfoWrapper<MapEntity> beanInfoWrapper = (BeanInfoWrapper<MapEntity>) BeanInfoWrapper.of(mapEntityClazz);
        List<String> beanNames =
                beanInfoWrapper.findAll()
                        .map(PropertyDescriptor::getName)
                        .filter(name -> !ignoreNames.contains(name))
                        .collectList()
                        .switchIfEmpty(Mono.justOrEmpty(Collections.emptyList()))
                        .block();
        for (String beanName : beanNames) {
            Object result = oriMap.get(beanName);
            if (result != null) {
                beanInfoWrapper.setCoverageForce(mapEntity, beanName, true, result);
            }
        }

        return mapEntity;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return new MapEntityDeserializer((Class<? extends MapEntity>) ctxt.getContextualType().getRawClass());
    }
}
