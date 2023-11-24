package cc.allio.uno.turbo.modules.system.entity;

import cc.allio.uno.turbo.common.mybatis.constraint.Unique;
import cc.allio.uno.turbo.common.mybatis.entity.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_org")
@Schema(description = "系统组织")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrg extends TreeEntity {

    /**
     * 编码
     */
    @TableField("code")
    @Schema(description = "编码")
    @NotNull
    @Unique
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    @Schema(description = "名称")
    private String name;

    /**
     * 描述
     */
    @TableField("des")
    @Schema(description = "描述")
    private String des;

    /**
     * 排序
     */
    @TableField("sort")
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 组织类型
     */
    @TableField("type")
    @Schema(description = "组织类型")
    private String type;
}
