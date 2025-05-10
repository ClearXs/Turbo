package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.modules.system.enums.DicType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_dic")
@Schema(description = "系统菜单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDic extends TreeEntity {

    /**
     * 字典类型
     */
    @TableField("type")
    @Schema(description = "字典类型")
    @NotNull
    private DicType type;

    /**
     * 字典编码
     */
    @TableField("code")
    @Schema(description = "字典编码")
    @NotNull
    @Unique
    private String code;

    /**
     * 字典名称
     */
    @TableField("name")
    @Schema(description = "字典名称")
    @NotNull
    private String name;

    /**
     * 字典描述
     */
    @TableField("des")
    @Schema(description = "字典描述")
    private String des;

    /**
     * 字典排序
     */
    @TableField("sort")
    @Schema(description = "字典排序")
    @Sortable
    private Integer sort;

    /**
     * 颜色
     */
    @TableField("color")
    @Schema(description = "颜色")
    private String color;
}
