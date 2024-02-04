package cc.allio.turbo.common.jackson;

import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.uno.core.bean.BeanInfoWrapper;
import cc.allio.uno.core.bean.ValueWrapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 基于{@link java.beans.BeanInfo}当map数据序列化时放入map中使之借助{@link MapSerializer}转换为json数据
 *
 * @author jiangwei
 * @date 2024/2/2 23:58
 * @since 0.1.0
 */
public class MapEntitySerializer extends StdSerializer<MapEntity> {
    private static final List<String> ignoreProperty = List.of("empty", "class");
    private static final MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class);

    public MapEntitySerializer() {
        super(mapType);
    }

    @Override
    public void serialize(MapEntity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        MapSerializer serializer =
                MapSerializer.construct(
                        Collections.emptySet(),
                        null,
                        mapType,
                        false,
                        null,
                        new StdKeySerializers.StringKeySerializer(),
                        null,
                        null);
        // bean数据放入map
        BeanInfoWrapper<? extends MapEntity> beanInfoWrapper = BeanInfoWrapper.of(value.getClass());
        List<String> beanNames =
                beanInfoWrapper.findAll()
                        .filter(p -> !ignoreProperty.contains(p.getName()))
                        .map(PropertyDescriptor::getName)
                        .collectList()
                        .switchIfEmpty(Mono.justOrEmpty(Collections.emptyList()))
                        .block();
        for (String beanName : beanNames) {
            Object result = beanInfoWrapper.getForce(value, beanName);
            if (ValueWrapper.EMPTY_VALUE == result) {
                value.put(beanName, null);
            } else {
                value.put(beanName, result);
            }
        }
        serializer.serialize(value, gen, provider);
    }
}
