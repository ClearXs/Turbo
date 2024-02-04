package cc.allio.turbo.modules.developer.domain.view;

import lombok.Data;

@Data
public class TreeToolbar {

    // 是否显示增加按钮
    private boolean showAdd;
    // 是否显示全选按钮，当属性multiple = true时生效
    private boolean showSelectAll;
    // 是否显示取消全选按钮，当属性multiple = true时生效
    private boolean showUnSelectAll;
    // 自定义追加
    private TreeToolbar[] append;
}
