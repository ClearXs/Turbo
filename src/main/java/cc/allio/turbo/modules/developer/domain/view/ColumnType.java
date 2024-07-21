package cc.allio.turbo.modules.developer.domain.view;

import cc.allio.turbo.common.db.constant.FieldType;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColumnType {

    Input("input", "文本输入", FieldType.VARCHAR),
    Number("number", "数字输入", FieldType.BIGINT),
    Textarea("textarea", "多行输入", FieldType.LONGVARCHAR),
    Password("password", "密码输入", FieldType.VARCHAR),
    Rate("rate", "评分器", FieldType.DOUBLE),
    Slider("slider", "滑动条", FieldType.DOUBLE),
    Select("select", "选择框", FieldType.VARCHAR),
    TreeSelect("treeSelect", "树选择", FieldType.VARCHAR),
    Cascade("cascade", "集联选择", FieldType.VARCHAR),
    Transfer("transfer", "穿梭框", FieldType.VARCHAR),
    Radio("radio", "单选框", FieldType.VARCHAR),
    Checkbox("checkbox", "复选框", FieldType.VARCHAR),
    Switch("switch", "开关", FieldType.BIT),
    Date("date", "日期选择", FieldType.DATE),
    DateRange("dateRange", "日期范围", FieldType.DATE),
    Time("time", "时间", FieldType.DATE),
    TimeRange("timeRange", "时间范围", FieldType.DATE),
    Upload("upload", "上传", FieldType.LONGVARCHAR),
    UploadDrag("uploadDrag", "拖拽上传", FieldType.LONGVARCHAR),
    Icon("icon", "图表", FieldType.LONGVARCHAR),
    Color("color", "颜色选择", FieldType.LONGVARCHAR),
    JsonObject("jsonObject", "jsonObject", FieldType.LONGVARCHAR),
    JsonArray("jsonArray", "jsonObject", FieldType.LONGVARCHAR),
    User("user", "user", FieldType.BIGINT),
    Org("org", "org", FieldType.BIGINT),
    Post("post", "post", FieldType.BIGINT),
    Role("role", "role", FieldType.BIGINT),
    CodeEditor("codeEditor", "代码编辑器", FieldType.LONGVARCHAR),
    Slot("slot", "自定义组件", FieldType.LONGVARCHAR),
    Undefined("undefined", "未定义", FieldType.LONGVARCHAR);

    @JsonValue
    private final String value;
    private final String label;
    private final FieldType fieldType;
}
