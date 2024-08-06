package cc.allio.turbo.modules.office.dto.page;

import cc.allio.turbo.modules.office.entity.Doc;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Schema(description = "文档分页查询")
@EqualsAndHashCode(callSuper = true)
public class DocPageDTO extends Page<Doc> {

    /**
     * 文档名称
     */
    @Schema(description = "文档名称")
    private String title;

    /**
     * 文档类型
     */
    @Schema(description = "文档类型")
    private List<String> type;

    /**
     * 文档创建者
     */
    @Schema(description = "文档创建者")
    private Long creator;

    /**
     * 文档协作者
     */
    @Schema(description = "文档协作者")
    private Long collaborator;

    /**
     * 是否分享
     */
    @Schema(description = "是否分享")
    private Boolean shared;

    /**
     * 是否喜欢
     */
    @Schema(description = "是否喜欢")
    private Boolean favorite;

    /**
     * 是否常用
     */
    @Schema(description = "是否常用")
    private Boolean favor;
}
