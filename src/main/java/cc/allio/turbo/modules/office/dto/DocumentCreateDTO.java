package cc.allio.turbo.modules.office.dto;

import cc.allio.turbo.modules.office.constant.DocType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "创建文档")
public class DocumentCreateDTO {

    /**
     * 文档名称
     */
    @NotBlank
    private String title;

    /**
     * 文档类型
     */
    @NotNull
    private DocType type;
}
