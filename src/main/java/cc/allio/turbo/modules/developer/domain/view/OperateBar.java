package cc.allio.turbo.modules.developer.domain.view;

import lombok.Data;

@Data
public class OperateBar {

    // 是否显示编辑操作
    private Object showEdit;
    // 是否显示删除操作
    private Object showDelete;
    // 是否显示详情
    private Object showDetails;
    // 自定义追加，当是函数渲染时，返回值如果是undefined 该追加操作则不进行添加
    private Object[] append;
}
