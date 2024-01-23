package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.uno.core.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 业务对象Schema，一个领域对象。用于描述业务对象
 *
 * @author jiangwei
 * @date 2024/1/18 16:36
 * @since 0.1.0
 */
@Data
public class BoSchema implements Serializable {

    // bo id
    private Long id;
    // bo code
    private String code;
    // bo name
    private String name;

    /**
     * props
     */
    private Map<String, Object> props;

    /**
     * attr schema
     */
    private List<BoAttrSchema> attrs;

    /**
     * 根据Json文本信息转换为{@link BoSchema}
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
        boSchema.setId(bo.getId());
        boSchema.setCode(bo.getCode());
        boSchema.setName(bo.getName());
        List<BoAttrSchema> attrSchemas = BoAttrSchema.from(treeify);
        boSchema.setAttrs(attrSchemas);
        return boSchema;
    }

}
