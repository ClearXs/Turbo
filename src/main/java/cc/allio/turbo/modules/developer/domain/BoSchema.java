package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 业务对象Schema，一个领域对象。用于描述业务对象
 *
 * @author j.x
 * @date 2024/1/18 16:36
 * @since 0.1.0
 */
@Data
public class BoSchema implements Serializable, Entity {

    // bo id
    private String id;
    // bo code
    private String code;
    // bo name
    private String name;

    // 物化
    private Boolean materialize;

    // 数据源信息
    private Long dataSourceId;

    /**
     * props
     */
    private Map<String, Object> props = Maps.newHashMap();

    /**
     * attr schema
     */
    private List<BoAttrSchema> attrs = Lists.newArrayList();

    /**
     * 根据Json bo schema转换为{@link BoSchema}
     *
     * @param text json
     * @return BoSchema
     */
    public static BoSchema from(String text) {
        return JsonUtils.parse(text, BoSchema.class);
    }

    /**
     * 根据 bo attribute转换为 {@code BoSchema}
     *
     * @param bo      bo
     * @param treeify treeify
     * @return BoSchema
     */
    public static BoSchema from(DevBo bo, List<BoAttributeTree> treeify) {
        BoSchema boSchema = new BoSchema();
        boSchema.setId(bo.getId().toString());
        boSchema.setCode(bo.getCode());
        boSchema.setName(bo.getName());
        boSchema.setDataSourceId(bo.getDataSourceId());
        boSchema.setMaterialize(bo.isMaterialize());
        List<BoAttrSchema> attrSchemas = BoAttrSchema.from(treeify);
        boSchema.setAttrs(attrSchemas);
        return boSchema;
    }

    /**
     * from {@link TableColumns} create new instance of {@link BoSchema}
     *
     * @param tableColumns the {@link TableColumns} instance
     * @return the {@link BoSchema} instance
     */
    public static BoSchema from(TableColumns tableColumns) {
        BoSchema boSchema = new BoSchema();
        Table table = tableColumns.getTable();
        boSchema.setCode(table.getName().format());
        boSchema.setName(table.getAlias());
        List<ColumnDef> columnDefs = tableColumns.getColumnDefs();
        List<BoAttrSchema> attrs = columnDefs.stream().map(BoAttrSchema::from).toList();
        boSchema.setAttrs(attrs);
        return boSchema;
    }

    /**
     * 获取primary bo attr schema
     * <p>{@link #attrs}中第一个是{@link cc.allio.turbo.modules.developer.constant.AttributeType#TABLE}的数据</p>
     *
     * @return BoAttrSchema or null
     */
    @JsonIgnore
    public BoAttrSchema primarySchema() {
        for (BoAttrSchema attr : attrs) {
            if (AttributeType.TABLE == attr.getAttrType()) {
                return attr;
            }
        }
        return null;
    }
}
