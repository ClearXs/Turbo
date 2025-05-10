package cc.allio.turbo.modules.development.domain.view;

import lombok.Data;

@Data
public class Search {

    // 是否显示search
    private Boolean show;
    // 是否禁用search字段，默认为false
    private Boolean disabled;
    // 是否显示search按钮，默认为true
    private Boolean showSearch;
    // 是否显示reset按钮，默认为true
    private Boolean showReset;
}
