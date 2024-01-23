package cc.allio.turbo.common.domain;

import cc.allio.turbo.common.db.entity.TreeNodeEntity;
import cc.allio.uno.core.datastructure.tree.ComparableElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.Date;

/**
 * 领域树模型，提供平展树结构转换为层次树结构
 *
 * @param <T> 自身树类型
 * @param <R> 平展结构类型
 * @author j.x
 * @date 2023/11/9 15:36
 * @since 0.1.0
 */
public abstract class TreeDomain<T extends TreeNodeEntity, R extends TreeDomain<T, R> & TreeNodeEntity>
        extends ComparableElement<R>
        implements TreeNodeEntity {

    @JsonIgnore
    @Getter
    protected final T entity;

    @Setter
    @Getter
    @Schema(description = "id")
    private Long id;

    @Setter
    @Getter
    @Schema(description = "父级Id")
    private Long parentId;

    @Setter
    @Getter
    @Schema(description = "tenantId")
    private Long tenantId;

    /**
     * 创建时间
     */
    @Setter
    @Getter
    @Schema(description = "创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @Setter
    @Getter
    @Schema(description = "创建人")
    private Long createdBy;

    /**
     * 更新时间
     */
    @Setter
    @Getter
    @Schema(description = "更新时间")
    private Date updatedTime;

    /**
     * 更新人
     */
    @Setter
    @Getter
    @Schema(description = "更新人")
    private Long updatedBy;

    /**
     * 逻辑删除
     */
    @Setter
    @Getter
    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Setter
    @Getter
    @Schema(description = "版本号")
    private Integer version;

    protected TreeDomain() {
        this(null, null);
    }

    protected TreeDomain(T entity) {
        this(entity, null);
    }

    protected TreeDomain(T entity, Comparator<R> comparator) {
        super(entity != null ? entity.getId() : null, comparator);
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
        this.entity = entity;
    }

}
