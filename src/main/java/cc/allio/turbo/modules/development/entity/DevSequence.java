package cc.allio.turbo.modules.development.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.extension.swift.SwiftGenType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dev_sequence")
@Schema(description = "流水号")
public class DevSequence extends TenantEntity {

    /**
     * 业务Key
     */
    @TableField("key")
    @Schema(description = "业务Key")
    @NotBlank
    private String key;

    /**
     * 业务名称
     */
    @TableField("name")
    @Schema(description = "业务名称")
    @NotBlank
    private String name;

    /**
     * 初始值
     */
    @TableField("initial_value")
    @Schema(description = "初始值")
    private Integer initialValue;

    /**
     * 序号长度
     */
    @TableField("length")
    @Schema(description = "序号长度")
    private Integer length;

    /**
     * 序号前缀
     */
    @TableField("prefix")
    @Schema(description = "序号前缀")
    private String prefix;

    /**
     * 序号后缀
     */
    @TableField("suffix")
    @Schema(description = "序号后缀")
    private String suffix;

    /**
     * 步长
     */
    @TableField("step")
    @Schema(description = "步长")
    private Integer step;

    /**
     * 生成类型
     */
    @TableField("gen_type")
    @Schema(description = "生成类型")
    private SwiftGenType genType;
}
