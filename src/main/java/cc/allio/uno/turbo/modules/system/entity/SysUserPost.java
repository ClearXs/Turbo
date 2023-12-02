package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.entity.IdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("sys_user_post")
@Schema(description = "用户岗位关联")
public class SysUserPost extends IdEntity {

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 组织id
     */
    @TableField("post_id")
    @Schema(description = "岗位id")
    private Long postId;
}
