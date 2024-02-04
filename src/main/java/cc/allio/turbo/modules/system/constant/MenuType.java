package cc.allio.turbo.modules.system.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuType {

    MENU("MENU", "菜单"),
    BUTTON("BUTTON", "按钮"),
    PAGE("PAGE", "菜单");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;

}
