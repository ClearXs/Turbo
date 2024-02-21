package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_post")
@Schema(description = "系统岗位")
public class SysPost extends TenantEntity {

    /**
     * 岗位名称
     */
    @TableField("name")
    @Schema(description = "岗位名称")
    @NotBlank
    private String name;

    /**
     * 岗位编码
     */
    @TableField("code")
    @Schema(description = "岗位编码")
    @NotBlank
    @Unique
    private String code;

    /**
     * 岗位描述
     */
    @TableField("des")
    @Schema(description = "岗位描述")
    private String des;

    /**
     * 排序
     */
    @TableField("sort")
    @Schema(description = "排序")
    @Sortable
    private Integer sort;
}
