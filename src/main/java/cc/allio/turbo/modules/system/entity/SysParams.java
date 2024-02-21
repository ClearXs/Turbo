package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.constraint.Unique;
import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_params")
@Schema(description = "系统参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysParams extends TenantEntity {

    /**
     * 参数名称
     */
    @TableField("name")
    @Schema(description = "参数名称")
    @NotNull
    private String name;

    /**
     * 参数key
     */
    @TableField("key")
    @Schema(description = "参数key")
    @NotNull
    @Unique
    private String key;

    /**
     * 参数值
     */
    @TableField("value")
    @Schema(description = "参数值")
    @Sortable
    private String value;

    /**
     * 参数描述
     */
    @TableField("description")
    @Schema(description = "参数描述")
    @Sortable
    private String description;
}
