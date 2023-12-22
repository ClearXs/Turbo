package cc.allio.turbo.modules.system.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 云存储类型
 *
 * @author j.x
 * @date 2023/11/16 17:46
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum CsType {

    /**
     * Object Storage Service 对象关系存储
     */
    OSS("OSS", "对象关系存储"),

    /**
     * Network Attached Storage 网络附加存储
     */
    NAS("NAS", "网络附加存储"),

    /**
     * Elastic Block Storage 块存储
     */
    EBS("EBS", "块存储");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
