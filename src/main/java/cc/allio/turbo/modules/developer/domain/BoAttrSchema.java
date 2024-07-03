package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.domain.Domains;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.common.db.constant.FieldType;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class BoAttrSchema implements Serializable, Entity {

    // bo attr id
    private String id;
    // bo attr key
    private String key;
    // bo attr field
    private String field;
    // bo attr name
    private String name;
    // attr type
    private AttributeType attrType;
    // bo attr 是否是root
    private boolean root;
    // bo attr 是否是leaf
    private boolean leaf;
    // bo attr field type
    private FieldType type;
    // bo attr parentId
    private Long parentId;
    // bo attr depth
    private Integer depth;
    // bo attr icon
    private String icon;
    // bo attr precision
    private Integer precision;
    // 预留属性，目前还未能赋值
    private Integer span;
    // 生命周期，拥有temporary与persistent两种值
    private String lifecycle;
    // bo attr scala
    private Integer scale;
    private boolean defaulted;
    private boolean binding;
    private boolean pk;
    private boolean fk;
    private boolean nonNull;
    private boolean unique;
    // bo attr props即Schema props
    private Map<String, Object> props = Collections.emptyMap();
    // 子
    private List<BoAttrSchema> children = Collections.emptyList();

    /**
     * 获取secondary schema
     * <p>在{@link #children}中寻找是{@link AttributeType#TABLE}的数据</p>
     *
     * @return list schema
     */
    @JsonIgnore
    public List<BoAttrSchema> obtainSecondarySchema() {
        return children.stream()
                .filter(child -> AttributeType.TABLE == child.attrType)
                .toList();
    }

    /**
     * 在{@link #children}中寻找是{@link AttributeType#FIELD}的数据
     *
     * @return list schema
     */
    @JsonIgnore
    public List<BoAttrSchema> obtainFieldSchema() {
        return children.stream()
                .filter(child -> AttributeType.FIELD == child.attrType)
                .toList();
    }

    /**
     * transfer bo type to java type
     *
     * @return the java type
     */
    public Class<?> getJavaType() {
        if (type == null) {
            return Object.class;
        }
        int jdbcType = type.getDslType().getJdbcType();
        return TypeRegistry.getInstance().findJavaType(jdbcType).getJavaType();
    }

    /**
     * 递归创建{@link BoAttrSchema}
     */
    public static List<BoAttrSchema> from(List<BoAttributeTree> treeify) {
        return treeify.stream()
                .map(tree -> {
                    BoAttrSchema attrSchema = BeanUtils.copy(tree, BoAttrSchema.class);
                    attrSchema.setId(Types.toString(tree.getId()));
                    attrSchema.setKey(tree.getCode());
                    if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                        List<BoAttrSchema> children = BoAttrSchema.from(tree.getChildren());
                        attrSchema.setChildren(children);
                    }
                    return attrSchema;
                })
                .toList();
    }


    /**
     * from {@link ColumnDef} create new instance of {@link BoAttrSchema}
     *
     * @param columnDef the {@link ColumnDef} instance
     * @return the {@link BoAttrSchema} instance
     */
    public static BoAttrSchema from(ColumnDef columnDef) {
        BoAttrSchema boAttrSchema = new BoAttrSchema();
        boAttrSchema.setId(columnDef.getDslName().format());
        boAttrSchema.setKey(columnDef.getDslName().format());
        boAttrSchema.setField(columnDef.getDslName().format());
        boAttrSchema.setName(columnDef.getComment());
        boAttrSchema.setAttrType(AttributeType.FIELD);
        DataType dataType = columnDef.getDataType();
        boAttrSchema.setType(FieldType.castTo(dataType));
        boAttrSchema.setPk(columnDef.isPk());
        boAttrSchema.setFk(columnDef.isFk());
        boAttrSchema.setNonNull(columnDef.isNonNull());
        boAttrSchema.setUnique(columnDef.isUnique());
        return boAttrSchema;
    }

    /**
     * 递归创建{@link BoAttrSchema}
     *
     * @param schemas schemas
     * @return BoAttributeTree for list
     */
    public static List<BoAttributeTree> to(List<BoAttrSchema> schemas) {
        return schemas.stream()
                .map(schema -> {
                    BoAttributeTree attrTree = to(schema);
                    if (CollectionUtils.isNotEmpty(schema.children)) {
                        List<BoAttributeTree> children = to(schema.children);
                        attrTree.setChildren(children);
                    }
                    return attrTree;
                })
                .toList();
    }

    /**
     * 创建{@link BoAttributeTree}
     *
     * @param schema schema
     * @return BoAttributeTree
     */
    public static BoAttributeTree to(BoAttrSchema schema) {
        return Domains
                .toEntity(schema, BoAttributeTree.class, (o, tree) -> {
                    tree.setId(Long.valueOf(o.id));
                    return tree;
                });
    }
}
