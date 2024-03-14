package cc.allio.turbo.common.db.entity;

import cc.allio.turbo.common.jackson.MapEntityDeserializer;
import cc.allio.turbo.common.jackson.MapEntitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 基于HashMap创建的实体，它可以有效解决位置属性的映射。
 * <p>比如说:</p>
 * <ul>
 *     <li>业务对象的映射</li>
 *     <li>json属性集的映射</li>
 *     <li>作为一种类型用作于范型域</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/2/2 23:40
 * @since 0.1.0
 */
@Data
@JsonSerialize(using = MapEntitySerializer.class)
@JsonDeserialize(using = MapEntityDeserializer.class)
public abstract class MapEntity extends HashMap<String, Object> implements Entity {

    @Override
    public Serializable getId() {
        Object id = get("id");
        if (id != null) {
            return (Serializable) id;
        }
        return null;
    }
}
