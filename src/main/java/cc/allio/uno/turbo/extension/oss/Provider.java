package cc.allio.uno.turbo.extension.oss;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 云存储提供商
 *
 * @author j.x
 * @date 2023/11/16 17:46
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum Provider {

    MINIO("MINIO", "MINIO"),
    ALIYUN("ALIYUN", "阿里云oss");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
