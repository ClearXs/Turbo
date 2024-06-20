package cc.allio.turbo.modules.developer.domain.view;

import cc.allio.turbo.common.db.entity.MapEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视图数据字段列定义
 *
 * @author j.x
 * @date 2024/2/3 19:48
 * @since 0.1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FieldColumn extends MapEntity {

    // 字段标识
    private String field;
    // 字段位置索引
    private Integer index;
    // 字段类型
    private String type;
    // 字段名称
    private String label;
    // 快捷的表单必填项，如果search = true，则默认 = false
    private Object require;
    // 表格表单校验规则
    private Object[] rules;
    // 表单字段初始值
    private Object initValue;
    // 表单文本拓展
    private String extraText;
    // 行分割的数目，默认为12
    private String span;
    // 是否单独一行
    private boolean line;
    // 当前column是否在form中显示，默认为true
    private Object form;
    // 触发校验的时机 触发校验的时机，可选值:blur/change/custom/mount，或以上值的组合['blur','change']
    // 1、设置为 custom 时，仅会由 formApi/fieldApi 触发校验时被触发
    // 2、mount（挂载时即触发一次校验）
    private Object validateTrigger;
    // 自定义校验
    private Object validate;
    // 联动
    private Object reaction;
    // 关联
    private Object relation;
    // 数据字段
    private String dataIndex;
    // 当前column是否在table中显示，默认为true
    private Object table;
    // 是否支持搜索，如果true则把column展示在搜索栏中，默认false
    private Object search;
    // whether column editable
    private Boolean editable;
}
