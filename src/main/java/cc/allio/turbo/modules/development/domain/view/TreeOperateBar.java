package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

import java.io.Serializable;

@Data
public class TreeOperateBar implements Serializable {

    // 是否显示怎增加操作
    private Object showAdd;
    // 是否显示编辑操作
    private Object showEdit;
    // 是否显示删除操作
    private Object showDelete;
    // 是否显示怎详情操作
    private Object showDetails;
    // 自定义追加
    private Object[] append;
}
