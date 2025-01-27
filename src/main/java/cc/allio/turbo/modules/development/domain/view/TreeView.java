package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 树型视图定义
 *
 * @author j.x
 * @date 2024/2/3 19:48
 * @since 0.1.0
 */
@Data
public class TreeView implements Serializable {

    // 用于初始化查询参数，如果存在的话
    private Map<String, Object> params;
    // 用于在添加时的默认值
    private Map<String, Object> addDefaultValue;
    // 字段集合
    private FieldColumn[] columns;
    private TreeToolbar toolbar;
    private TreeOperateBar operateBar;
    // 是否展开所有项
    private boolean expandAll;
    // 需求树结点深度，如果不传默认所有
    private int depth;
    // 是否多选
    private boolean multiple;
    // 初始值（list key值），开启multiple时生效
    private String[] initValues;
    // 是否选中第一个
    private boolean first;
    // 初始化值
    private String initValue;
    // 根结点名称，如果赋值将会创建一个ROOT的虚拟结点
    private String root;
}
