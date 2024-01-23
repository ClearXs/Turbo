package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.CollectionUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public final class BoAttrSchema implements Serializable {

    private BoAttrSchema() {
    }

    // bo attr id
    private Long id;
    // bo attr key
    private String key;
    // bo attr name
    private String name;
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
    // bo attr scala
    private Integer scala;
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
                    attrSchema.setKey(tree.getCode());
                    if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                        List<BoAttrSchema> children = BoAttrSchema.from(tree.getChildren());
                        attrSchema.setChildren(children);
                    }
                    return attrSchema;
                })
                .toList();
    }
}
