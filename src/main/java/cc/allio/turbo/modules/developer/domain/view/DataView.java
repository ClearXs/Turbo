package cc.allio.turbo.modules.developer.domain.view;

import lombok.Data;

import java.util.Map;

/**
 * 数据视图定义
 *
 * @author jiangwei
 * @date 2024/2/2 17:54
 * @since 0.1.0
 */
@Data
public class DataView {

    // 视图id
    private String id = "id";
    // 视图模式
    private ViewMode mode = ViewMode.PAGE;
    // 工具栏
    private Toolbar toolbar;
    // 字段集合
    private FieldColumn[] columns;
    // 操作栏
    private OperateBar operateBar;
    // 初始化参数
    private Map<String, Object> params;
    // 左树
    private TreeView leftTree;
    // 卡片视图模式下生效
    private Card card;
    // 弹窗
    private Modal modal;
}
