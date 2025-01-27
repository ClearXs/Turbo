package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.modules.development.constant.AttributeType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("dev_bo_attribute")
@Schema(description = "业务对象属性")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DevBoAttribute extends TreeEntity {

    /**
     * 业务对象属性名称
     */
    @TableField("name")
    @Schema(description = "业务对象属性名称")
    @NotBlank
    private String name;

    /**
     * 业务对象属性编码
     */
    @TableField("code")
    @Schema(description = "业务对象属性编码")
    @NotBlank
    private String code;

    /**
     * 业务对象属性字段
     */
    @TableField("field")
    @Schema(description = "业务对象属性字段")
    @NotBlank
    private String field;

    /**
     * 业务对象属性类型
     */
    @TableField("attr_type")
    @Schema(description = "业务对象属性类型")
    @NotBlank
    private AttributeType attrType;

    /**
     * 业务对象属性类型
     */
    @TableField("props")
    @Schema(description = "业务对象属性参数值")
    private String props;

    /**
     * 业务对象属性备注
     */
    @TableField("remark")
    @Schema(description = "业务对象属性备注")
    private String remark;

    /**
     * 所属于业务对象表ID
     */
    @TableField("bo_id")
    @Schema(description = "所属于业务对象表ID")
    private Long boId;

}
