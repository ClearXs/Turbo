package cc.allio.turbo.modules.developer.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * code template domain
 *
 * @author j.x
 * @date 2024/5/3 15:14
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public enum CodeTemplateDomain {

    FRONTEND("frontend", "前端域"),
    BACKEND("backend", "后端域"),
    FILE("file", "文件域"),
    OTHER("other", "其他");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
