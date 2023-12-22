package cc.allio.turbo.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 绑定角色
 *
 * @author j.x
 * @date 2023/11/9 22:36
 * @since 0.1.0
 */
@Data
@Schema(description = "绑定角色数据传输对象")
public class BindingRoleDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @NotNull
    private Long userId;

    /**
     * 角色ids
     */
    @Schema(description = "角色ids")
    @NotNull
    private List<Long> roleIds;
}

