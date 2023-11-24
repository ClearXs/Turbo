package cc.allio.uno.turbo.common.mybatis.entity;

import cc.allio.uno.core.datastructure.tree.Expand;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity extends TenantEntity implements Expand {

    /**
     * 父级菜单
     */
    @TableField("parent_id")
    @Schema(description = "父级菜单")
    private Long parentId;
}
