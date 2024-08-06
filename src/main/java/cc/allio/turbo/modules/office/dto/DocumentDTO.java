package cc.allio.turbo.modules.office.dto;

import cc.allio.turbo.modules.office.vo.DocVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentDTO extends DocVO {

    /**
     * 创建者名称
     */
    @Schema(name = "创建者名称")
    private String createName;

    /**
     * 协作者名称
     */
    @Schema(name = "协作者名称")
    private String collaboratorName;
}