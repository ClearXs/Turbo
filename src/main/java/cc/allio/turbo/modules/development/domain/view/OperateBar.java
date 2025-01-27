package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

@Data
public class OperateBar {

    // 是否行内编辑
    private Boolean showInlineEdit;
    // 是否显示编辑操作
    private Boolean showEdit;
    // 是否显示删除操作
    private Boolean showDelete;
    // 是否显示详情
    private Boolean showDetails;
    // 是否显示复制操作
    private Boolean showCopy;
    // 自定义追加，当是函数渲染时，返回值如果是undefined 该追加操作则不进行添加
    private Object[] append;

}
