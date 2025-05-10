package cc.allio.turbo.modules.development.domain;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.modules.development.enums.AttributeType;
import cc.allio.turbo.common.db.constant.FieldType;
import cc.allio.turbo.modules.development.entity.DevBoAttribute;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.DataType;
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

    /**
     * 是否为默认属性
     */
    @Schema(description = "是否为默认属性")
    private boolean defaulted;

    public BoAttributeTree() {
        super(new DevBoAttribute(), null);
    }

    public BoAttributeTree(DevBoAttribute expand) {
        super(expand, Comparator.comparing(BoAttributeTree::getName));
    }

    /**
     * 当类型是{@link AttributeType#FIELD}基于当前对象数据返回{@link ColumnDef}
     *
     * @return ColumnDef or null
     */
    public ColumnDef toColumnDef() {
        if (attrType != AttributeType.FIELD) {
            return null;
        }
        // 赋了一个默认的precision域scale
        DataType dataType = DataType.create(type.getDslType());
        if (precision != null) {
            dataType.setPrecision(precision);
        }
        if (scale != null) {
            dataType.setScale(scale);
        }
        return ColumnDef.builder()
                .dslName(DSLName.of(field, DSLName.LOWER_CASE_FEATURE, DSLName.UNDERLINE_FEATURE))
                .isPk(pk)
                .isFk(fk)
                .isUnique(unique)
                .isNonNull(nonNull)
                .dataType(dataType)
                .comment(name)
                .build();
    }
}
