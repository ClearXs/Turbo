package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_category")
@Schema(description = "系统种类")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysCategory extends TreeEntity {

    /**
     * 分类名称
     */
    @TableField("name")
    @Schema(description = "分类名称")
    @NotNull
    private String name;

    /**
     * 字典编码
     */
    @TableField("code")
    @Schema(description = "分类标识")
    @NotNull
    @Unique
    private String code;

    /**
     * 字典排序
     */
    @TableField("sort")
    @Schema(description = "字典排序")
    @Sortable
    private Integer sort;

    /**
     * 功能标识
     */
    @TableField("func_code")
    @Schema(description = "功能标识")
    private String funcCode;
}
