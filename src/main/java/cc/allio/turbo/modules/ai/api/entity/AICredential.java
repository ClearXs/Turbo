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
@TableName("ai_credential")
@Schema(description = "credential")
public class AICredential extends TenantEntity {

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 密钥
     */
    @TableField("secret_key")
    @Schema(description = "密钥")
    @NotBlank
    private String secretKey;

    /**
     * api key
     */
    @TableField("api_key")
    @Schema(description = "api key")
    private String apiKey;
}
