package cc.allio.turbo.modules.development.domain.hybrid;

import cc.allio.turbo.modules.development.domain.BoAttrSchema;
import cc.allio.turbo.modules.development.domain.BoSchema;
import cc.allio.turbo.modules.development.domain.view.DataView;
import cc.allio.turbo.modules.development.domain.view.FieldColumn;
import cc.allio.uno.core.util.tree.TreeSupport;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
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
     * composite {@link BoSchema} and {@link DataView} create new {@link HybridBoSchema}
     *
     * @param boSchema the {@link BoSchema} instance
     * @param dataView the {@link DataView} instance
     * @return the {@link HybridBoSchema} instance
     */
    public static HybridBoSchema composite(BoSchema boSchema, DataView dataView) {
        return composite(boSchema, dataView, c -> true);
    }

    /**
     * composite {@link BoSchema} and {@link DataView} create new {@link HybridBoSchema}
     *
     * @param boSchema the {@link BoSchema} instance
     * @param dataView the {@link DataView} instance
     * @param filter   if {@link FieldColumn} contains list of {@link BoAttrSchema}, then use {@code filter} further filter.
     * @return the {@link HybridBoSchema} instance
     */
    public static HybridBoSchema composite(BoSchema boSchema, DataView dataView, Predicate<FieldColumn> filter) {
        HybridBoSchema hybridBoSchema = new HybridBoSchema();
        hybridBoSchema.setId(boSchema.getId());
        hybridBoSchema.setCode(boSchema.getCode());
        hybridBoSchema.setName(boSchema.getName());
        hybridBoSchema.setMaterialize(boSchema.getMaterialize());
        hybridBoSchema.setProps(boSchema.getProps());
        hybridBoSchema.setDataSourceId(boSchema.getDataSourceId());
        hybridBoSchema.internal = dataView;
        List<BoAttrSchema> attrs = boSchema.getAttrs();
        // recursive get all attr
        Collection<BoAttrSchema> allAttr = TreeSupport.withExpandFn(attrs, BoAttrSchema::getChildren, t -> t);
        // transfer map from key
        var baseline = allAttr.stream().collect(Collectors.toMap(k -> k.getKey().getHump(), v -> v));
        FieldColumn[] fieldColumns = dataView.getColumns();
        for (FieldColumn field : fieldColumns) {
            // test contains attr and use filter test
            if (baseline.containsKey(field.getField()) && !filter.test(field)) {
                BoAttrSchema boAttrSchema = baseline.get(field.getField());
                HybridColumn column = HybridColumn.composite(boAttrSchema, field);
                hybridBoSchema.columns.add(column);
            }
        }
        return hybridBoSchema;
    }
}
