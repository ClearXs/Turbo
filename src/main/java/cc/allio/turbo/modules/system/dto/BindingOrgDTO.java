package cc.allio.turbo.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 绑定组织
 *
 * @author j.x
 * @date 2023/11/30 11:28
 * @since 0.1.0
 */
@Data
@Schema(description = "绑定组织数据传输对象")
public class BindingOrgDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @NotNull
    private Long userId;

    /**
     * 用户id
     */
    @Schema(description = "组织id")
    @NotNull
    private Long orgId;
}
