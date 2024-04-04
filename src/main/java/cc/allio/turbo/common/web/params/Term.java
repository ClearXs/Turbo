package cc.allio.turbo.common.web.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 与实体对应的查询条件。可以考虑做成通用查询构建，比如：
 * <ol>
 *     <li>增加条件类型 如 > < 包含 不包含</li>
 *     <li>增加条件之间的逻辑关系</li>
 * </ol>
 * 目前可以不考虑
 *
 * @author j.x
 * @date 2023/11/29 15:56
 * @since 0.1.0
 */
@Data
@Schema(description = "查询条件")
@AllArgsConstructor
@NoArgsConstructor
public class Term {

    /**
     * 字段
     */
    @Schema(description = "字段")
    private String field;

    /**
     * 值
     */
    @Schema(description = "值")
    private Object value;
}
