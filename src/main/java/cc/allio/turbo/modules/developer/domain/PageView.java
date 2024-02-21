package cc.allio.turbo.modules.developer.domain;

import cc.allio.turbo.modules.developer.domain.view.DataView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@link cc.allio.turbo.modules.developer.entity.DevPage}类的领域化
 * <p>提供页面视图相关数据</p>
 *
 * @author jiangwei
 * @date 2024/2/6 17:04
 * @since 0.1.0
 */
@Data
public class PageView {

    @Schema(description = "页面名称")
    private Long id;

    /**
     * 页面名称
     */
    @Schema(description = "页面名称")
    private String name;

    /**
     * 页面编码
     */
    @Schema(description = "页面编码")
    private String code;

    /**
     * 页面路由
     */
    @Schema(description = "页面路由")
    private String route;

    /**
     * 数据视图
     */
    @Schema(description = "数据视图")
    private DataView dataView;

    /**
     * 数据集
     */
    private Long datasetId;

    /**
     * bo
     */
    private Long boId;

    /**
     * 表单
     */
    private Long formId;
}
