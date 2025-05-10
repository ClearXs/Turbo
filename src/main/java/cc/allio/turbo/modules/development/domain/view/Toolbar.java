package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

import java.io.Serializable;

@Data
public class Toolbar implements Serializable {

    // 是否显示增加按钮
    private boolean showAdd;
    // 是否显示批量删除按钮
    private boolean showBatchDelete;
    // 是否显示导出按钮
    private boolean showExport;
    // 是否显示导入按钮
    private boolean showImport;
    // 是否展示显示列
    private boolean showColumns;
    // 是否展示排序
    private boolean showOrdered;
    // 自定义追加
    private Object[] append;

}
