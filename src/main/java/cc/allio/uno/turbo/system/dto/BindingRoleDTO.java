package cc.allio.uno.turbo.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 绑定角色
 *
 * @author j.x
 * @date 2023/11/9 22:36
 * @since 1.0.0
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
     * 用户id
     */
    @Schema(description = "角色id")
    @NotNull
    private List<Long> roleIds;
}

