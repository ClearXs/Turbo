package cc.allio.uno.turbo.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 权限授予 数据传输对象
 *
 * @author j.x
 * @date 2023/11/9 17:57
 * @since 0.1.0
 */
@Data
public class GrantPermissionDTO {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    @NotBlank
    private Long roleId;

    /**
     * 功能权限
     */
    @Schema(description = "菜单id")
    @NotBlank
    private List<Long> menuId;
}
