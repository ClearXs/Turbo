package cc.allio.turbo.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 绑定岗位
 *
 * @author j.x
 * @date 2023/12/1 11:34
 * @since 0.1.0
 */
@Data
@Schema(description = "绑定岗位数据传输对象")
public class BindingPostDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @NotNull
    private Long userId;

    /**
     * 岗位ids
     */
    @Schema(description = "岗位ids")
    @NotNull
    private List<Long> postIds;
}
