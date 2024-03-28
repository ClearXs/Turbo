package cc.allio.turbo.modules.message.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.message.template.Extension;
import cc.allio.turbo.modules.message.template.Variable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Table(name = "sys_message_template")
@EqualsAndHashCode(callSuper = true)
public class SysMessageTemplate extends TenantEntity {

    /**
     * 模板Key
     */
    @Schema(description = "模板Key")
    private String key;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    private String name;

    /**
     * 消息标题
     */
    @Schema(description = "消息标题")
    private String title;

    /**
     * 模板内容
     */
    @Schema(description = "模板内容")
    private String template;

    /**
     * 模板变量
     * <p>[{"key":"var1","des":"描述","defaultValue":""}]</p>
     */
    @Schema(description = "模板变量")
    private List<Variable> variables;

    /**
     * 拓展配置
     * <p>{"pcUrl":"","appUrl":""}</p>
     */
    @Schema(description = "模板变量")
    private Extension extension;
}
