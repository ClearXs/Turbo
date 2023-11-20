package cc.allio.uno.turbo.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(type = ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Long createdBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updatedTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    private Long updatedBy;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Version
    @TableField("version")
    @Schema(description = "版本号")
    private Integer version;
}
