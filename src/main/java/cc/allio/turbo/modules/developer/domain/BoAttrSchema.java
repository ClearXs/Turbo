package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.common.db.entity.Entity;
import cc.allio.turbo.common.domain.Domains;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public final class BoAttrSchema implements Serializable, Entity {

    private BoAttrSchema() {
    }

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
}
