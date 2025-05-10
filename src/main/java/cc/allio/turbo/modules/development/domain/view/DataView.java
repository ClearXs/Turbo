package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据视图定义
 *
 * @author j.x
 * @date 2024/2/2 17:54
 * @since 0.1.0
 */
@Data
public class DataView implements Serializable {

    // 视图id
    private String id = "id";
    // 视图模式
    private ViewMode mode = ViewMode.PAGE;
    // table width
    private String width;
    // table height
    private String height;
    // fixed table
    private Boolean fixed;
    // 字段集合
    private FieldColumn[] columns;
    // 是否具有可操作（意味着能否用是否api进行crud、能否点击操作按钮进行操作...）
    private Boolean operability;
    // whether disable assigned value in column
    private Boolean disableDefaultBehavior;
    // search
    private Search search;
    // 工具栏
    private Toolbar toolbar;
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
