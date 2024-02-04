package cc.allio.turbo.modules.developer.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务对象的领域实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Schema(description = "业务对象领域实体")
public class DomainEntity extends MapEntity implements TreeNodeEntity {

    /**
     * 该业务对象属于的pageId
     */
    @Schema(description = "该业务对象属于的pageId")
    private Serializable pageId;

    // =============== 基础属性 ===============

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private Long createdBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Sortable
    private Date updatedTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private Long updatedBy;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Schema(description = "版本号")
    private Integer version;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private Serializable tenantId;

    @Override
    public Serializable getParentId() {
        Object parentId = get("parentId");
        if (parentId != null) {
            return (Serializable) parentId;
        }
        return null;
    }
}
