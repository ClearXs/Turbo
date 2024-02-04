package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.modules.developer.constant.AttributeType;
import cc.allio.turbo.modules.developer.constant.FieldType;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Comparator;

@Setter
@Getter
@Accessors(chain = true)
public class BoAttributeTree extends TreeDomain<DevBoAttribute, BoAttributeTree> {

    /**
     * 业务对象属性名称
     */
    @Schema(description = "业务对象属性名称")
    @NotBlank
    private String name;

    /**
     * 业务对象属性编码
     */
    @Schema(description = "业务对象属性编码")
    @NotBlank
    private String code;

    /**
     * 业务对象属性字段
     */
    @Schema(description = "业务对象属性字段")
    @NotBlank
    private String field;

    /**
     * 业务对象属性类型
     */
    @Schema(description = "业务对象属性类型")
    @NotNull
    private AttributeType attrType;

    /**
     * 业务对象属性备注
     */
    @TableField("remark")
    @Schema(description = "业务对象属性备注")
    private String remark;

    /**
     * 所属于业务对象表ID
     */
    @TableField("boId")
    @Schema(description = "所属于业务对象表ID")
    private Long boId;

    /**
     * 业务对象属性类型
     */
    @Schema(description = "业务对象属性类型")
    private FieldType type;

    /**
     * 业务对象属性长度
     */
    @Schema(description = "业务对象属性长度")
    private Integer precision;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "业务对象属性小数位")
    private Integer scale;

    /**
     * 是否主键
     */
    @Schema(description = "是否主键")
    private boolean pk;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否外键")
    private boolean fk;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否非空")
    private boolean nonNull;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否唯一")
    private boolean unique;

    public BoAttributeTree() {
        super(new DevBoAttribute(), null);
    }

    public BoAttributeTree(DevBoAttribute expand) {
        super(expand, Comparator.comparing(BoAttributeTree::getName));
    }
}
