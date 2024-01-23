package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.common.domain.JsonDomain;
import cc.allio.turbo.modules.developer.constant.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * dev props
 *
 * @author jiangwei
 * @date 2024/1/18 17:29
 * @since 0.1.0
 */
@Data
@Accessors(chain = true)
public class DevAttributeProps extends JsonDomain implements Serializable {

    /**
     * 业务对象属性类型
     */
    @Schema(description = "业务对象属性类型")
    @NotBlank
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
    private Integer scala;

    /**
     * 是否主键
     */
    @Schema(description = "是否主键")
    private Boolean isPk;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否外键")
    private Boolean isFk;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否非空")
    private Boolean isNonNull;

    /**
     * 业务对象属性小数位
     */
    @Schema(description = "是否唯一")
    private Boolean isUnique;

}
