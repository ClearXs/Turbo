package cc.allio.turbo.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "修改密码")
public class ChangePasswordDTO {

    @Schema(description = "用户id")
    @NotNull
    private Long id;

    @Size(min = 6)
    @Schema(description = "原密码")
    private String rawPassword;

    @Size(min = 6)
    @NotNull
    @Schema(description = "新密码")
    private String newPassword;
}
