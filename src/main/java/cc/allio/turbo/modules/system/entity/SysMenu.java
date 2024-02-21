package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.modules.system.constant.MenuType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

@TableName("sys_menu")
@Schema(description = "系统菜单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends TreeEntity {

    /**
     * 菜单域
     *
     * @deprecated 没有详细说明该字段是什么意思
     */
    @TableField("scope")
    @Schema(description = "scope")
    @Deprecated
    private String scope;

    /**
     * 菜单名称
     */
    @TableField("code")
    @Schema(description = "菜单编码")
    @NotNull
    @Unique
    private String code;

    /**
     * 菜单名称
     */
    @TableField("name")
    @Schema(description = "菜单名称")
    @NotNull
    private String name;

    /**
     * 菜单类型
     */
    @TableField("type")
    @Schema(description = "菜单类型")
    @NotNull
    private MenuType type;

    /**
     * 菜单序号
     */
    @TableField("sort")
    @Schema(description = "菜单序号")
    @Sortable
    private Integer sort;

    /**
     * 别名
     */
    @TableField("alias")
    @Schema(description = "别名")
    private String alias;

    /**
     * 路径
     */
    @TableField("route")
    @Schema(description = "菜单路径")
    private String route;

    /**
     * icon
     */
    @TableField("icon")
    @Schema(description = "icon")
    private String icon;

    /**
     * attrs
     */
    @TableField(value = "attrs", jdbcType = JdbcType.ARRAY)
    @Schema(description = "attrs")
    private String[] attrs;
}
