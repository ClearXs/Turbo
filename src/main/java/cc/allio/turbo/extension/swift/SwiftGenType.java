package cc.allio.turbo.extension.swift;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwiftGenType {

    ALWAYS("ALWAYS", "递增"),
    YEAR("YEAR", "每年生成"),
    MONTH("MONTH", "每月生成"),
    DAY("DAY", "每日生成");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
