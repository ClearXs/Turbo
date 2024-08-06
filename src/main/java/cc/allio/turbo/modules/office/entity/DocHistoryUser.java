package cc.allio.turbo.modules.office.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("office_doc_history_user")
@Schema(description = "文档历史版本变更用户")
public class DocHistoryUser extends TenantEntity {

    /**
     * 文档历史版本id
     */
    @TableField("history_id")
    @Schema(name = "文档历史版本id")
    private Long historyId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(name = "用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @TableField("username")
    @Schema(name = "用户名称")
    private String username;

    /**
     * 变更时间
     */
    @TableField("changed_time")
    @Schema(name = "变更时间")
    private Date changedTime;
}
