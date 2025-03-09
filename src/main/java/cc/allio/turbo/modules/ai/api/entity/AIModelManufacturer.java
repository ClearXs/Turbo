package cc.allio.turbo.modules.ai.api.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ai_model_manufacturer")
@Schema(description = "模型厂商")
public class AIModelManufacturer extends TenantEntity {

    /**
     * 名称
     */
    @TableField("key")
    @Schema(description = "key")
    @NotBlank
    private String key;

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * icon
     */
    @TableField("icon")
    @Schema(description = "icon")
    private String icon;

    /**
     * readme
     */
    @TableField("readme")
    private String readme;

    /**
     * 名称
     */
    @TableField("credential_id")
    @Schema(description = "密钥Id")
    private Long credentialId;
}
