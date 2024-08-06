package cc.allio.turbo.modules.office.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DocType {

    DOC("doc", "doc"),
    DOCX("docx", "docx"),
    XLS("xls", "xls"),
    XLSX("xlsx", "xlsx"),
    PPT("ppt", "ppt"),
    PPTX("pptx", "pptx");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
