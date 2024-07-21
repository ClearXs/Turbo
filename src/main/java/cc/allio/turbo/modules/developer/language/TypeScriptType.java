package cc.allio.turbo.modules.developer.language;

import cc.allio.turbo.common.db.constant.FieldType;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeScriptType {

    Any("any", "any", null),
    Unknown("unknown", "unknown", null),
    Boolean("boolean", "boolean", FieldType.BOOLEAN),
    Number("number", "number", FieldType.NUMBER),
    Bigint("bigint", "bigint", FieldType.BIGINT),
    String("string", "string", FieldType.VARCHAR),
    Object("object", "object", FieldType.OBJECT);

    @JsonValue
    private final String value;
    private final String label;
    private final FieldType fieldType;
}
