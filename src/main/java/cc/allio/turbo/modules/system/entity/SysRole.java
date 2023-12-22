package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.mybatis.constraint.Sortable;
import cc.allio.turbo.common.mybatis.constraint.Unique;
import cc.allio.turbo.common.mybatis.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role")
@Schema(description = "系统角色")
public class SysRole extends TenantEntity {

    /**
     * 角色名称
     */
    @TableField("name")
    @Schema(description = "角色名称")
    @NotBlank
    private String name;

    /**
     * 角色名称
     */
    @TableField("code")
    @Schema(description = "角色编码")
    @NotBlank
    @Unique
    private String code;

    /**
     * 角色描述
     */
    @TableField("des")
    @Schema(description = "角色描述")
    private String des;

    /**
     * 排序
     */
    @TableField("sort")
    @Schema(description = "排序")
    @Sortable
    private Integer sort;
}
