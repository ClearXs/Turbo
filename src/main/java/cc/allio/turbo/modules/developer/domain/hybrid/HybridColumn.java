package cc.allio.turbo.modules.developer.domain.hybrid;

import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.view.FieldColumn;
import cc.allio.uno.core.util.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * aggregate {@link BoAttrSchema} and {@link FieldColumn} make it become new hybrid column
 *
 * @author j.x
 * @date 2024/6/18 21:51
 * @since 0.1.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HybridColumn extends BoAttrSchema {

    private FieldColumn internal;

    /**
     * composite {@link BoAttrSchema} and {@link FieldColumn} become new type of {@link HybridColumn}
     *
     * @param attrSchema the {@link BoAttrSchema} instance
     * @param field the {@link FieldColumn} instance
     * @return the {@link HybridColumn} instance
     */
    public static HybridColumn composite(BoAttrSchema attrSchema, FieldColumn field) {
        HybridColumn column = new HybridColumn();
        BeanUtils.copy(attrSchema, column);
        column.setInternal(field);
        return column;
    }
}
