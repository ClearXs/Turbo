package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.constraint.Sortable;
import cc.allio.turbo.common.db.entity.MapEntity;
import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefListResolve;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefResolve;
import cc.allio.uno.data.orm.dsl.helper.PojoInspection;
import cc.allio.uno.data.orm.dsl.helper.TableResolve;
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
@PojoInspection(DomainEntityPojoInspect.class)
@TableResolve(DomainEntityTableResolver.class)
@ColumnDefListResolve(DomainEntityColumnDefListResolver.class)
public class DomainEntity extends MapEntity implements TreeNodeEntity {

    public static final String SCHEMA = "schema";

    public static final String ID = "id";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";
    public static final String UPDATED_TIME = "updated_time";
    public static final String CREATED_TIME = "created_time";
    public static final String IS_DELETED = "is_deleted";
    public static final String VERSION = "version";
    public static final String TENANT_ID = "tenant_id";

    // 主键
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Long id;
    // 创建时间
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Date createdTime;
    // 创建人
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Long createdBy;
    // 更新时间
    @Sortable
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Date updatedTime;
    // 更新人
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Long updatedBy;
    // 逻辑删除
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Integer isDeleted;
    // 版本号
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Integer version;
    // 租户
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Serializable tenantId;
    // parentId
    @ColumnDefResolve(DomainEntityColumnDefResolver.class)
    private Long parentId;
}
