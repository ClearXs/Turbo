package cc.allio.turbo.modules.developer.domain.hybrid;

import cc.allio.turbo.modules.developer.domain.BoAttrSchema;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.domain.view.DataView;
import cc.allio.turbo.modules.developer.domain.view.FieldColumn;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * aggregate {@link BoSchema} and {@link DataView}
 *
 * @author j.x
 * @date 2024/6/18 22:06
 * @since 0.1.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HybridBoSchema extends BoSchema {

    /**
     * the hybrid columns
     */
    private List<HybridColumn> columns = Lists.newArrayList();

    /**
     * the wrap {@link DataView} in {@link HybridBoSchema}
     */
    private DataView internal;

    /**
     * compose {@link BoSchema} and {@link DataView} create new {@link HybridBoSchema}
     *
     * @param boSchema the {@link BoSchema} instance
     * @param dataView the {@link DataView} instance
     * @return the {@link HybridBoSchema} instance
     */
    public static HybridBoSchema compose(BoSchema boSchema, DataView dataView) {
        HybridBoSchema hybridBoSchema = new HybridBoSchema();
        hybridBoSchema.setId(boSchema.getId());
        hybridBoSchema.setCode(boSchema.getCode());
        hybridBoSchema.setName(boSchema.getName());
        hybridBoSchema.setMaterialize(boSchema.getMaterialize());
        hybridBoSchema.setProps(boSchema.getProps());
        hybridBoSchema.setDataSourceId(boSchema.getDataSourceId());
        hybridBoSchema.internal = dataView;
        List<BoAttrSchema> attrs = boSchema.getAttrs();
        Collection<BoAttrSchema> allAttr = TreeSupport.withExpandFn(attrs, BoAttrSchema::getChildren, t -> t);
        var keySchema = allAttr.stream().collect(Collectors.toMap(BoAttrSchema::getKey, v -> v));
        FieldColumn[] fieldColumns = dataView.getColumns();
        for (FieldColumn field : fieldColumns) {
            BoAttrSchema boAttrSchema = keySchema.get(field.getField());
            if (boAttrSchema != null) {
                HybridColumn column = HybridColumn.compose(boAttrSchema, field);
                hybridBoSchema.columns.add(column);
            }
        }
        return hybridBoSchema;
    }
}
