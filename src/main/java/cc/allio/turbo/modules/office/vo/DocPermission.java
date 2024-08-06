package cc.allio.turbo.modules.office.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文档权限
 *
 * @author j.x
 * @date 2024/5/9 19:22
 * @since 0.0.1
 */
@Data
public class DocPermission {

    /**
     * 评论
     */
    @Schema(name = "评论")
    private Boolean comment;

    /**
     * 复制
     */
    @Schema(name = "复制")
    private Boolean copy;

    /**
     * 编辑
     */
    @Schema(name = "编辑")
    private Boolean edit;

    /**
     * 打印
     */
    @Schema(name = "打印")
    private Boolean print;

    /**
     * 填写表格
     */
    @Schema(name = "填写表格")
    private Boolean fillForms;

    /**
     * 修改过滤
     */
    @Schema(name = "修改过滤")
    private Boolean modifyFilter;

    /**
     * 修改过滤内容
     */
    @Schema(name = "修改过滤内容")
    private Boolean modifyFilterContent;

    /**
     * review
     */
    @Schema(name = "review")
    private Boolean review;

    /**
     * 聊天
     */
    @Schema(name = "chat")
    private Boolean chat;

    /**
     * 模版
     */
    @Schema(name = "template")
    private Boolean templates;
}
