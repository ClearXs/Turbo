package cc.allio.turbo.common.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TreeEntity extends TenantEntity implements TreeNodeEntity {

    /**
     * 父级id
     */
    @TableField("parent_id")
    @Schema(description = "父项")
    private Long parentId;
}
