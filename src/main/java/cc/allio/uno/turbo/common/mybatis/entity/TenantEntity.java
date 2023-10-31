package cc.allio.uno.turbo.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户实体
 *
 * @author j.x
 * @date 2023/10/22 11:54
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantEntity extends BaseEntity {

    @TableField("tenant_id")
    @Schema(description = "租户")
    private String tenantId;
}
