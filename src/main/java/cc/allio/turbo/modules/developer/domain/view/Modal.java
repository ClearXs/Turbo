package cc.allio.turbo.modules.developer.domain.view;

import lombok.Data;

@Data
public class Modal {

    // 是否显示确认操作
    private Object showConfirm;
    // 是否显示取消操作
    private Object showCancel;
    // 自定义追加，当是函数渲染时，返回值如果是undefined 该追加操作则不进行添加
    private Object[] append;
}
