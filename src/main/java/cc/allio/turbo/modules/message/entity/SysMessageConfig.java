package cc.allio.turbo.modules.message.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.message.config.RetryFailed;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.SendTemplate;
import cc.allio.turbo.modules.message.constant.Enabled;
import cc.allio.turbo.modules.message.constant.NotificationType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 消息配置
 *
 * @author jiangwei
 * @date 2022/12/2 20:19
 * @since 2.9.0-RELEASE
 */
@Data
@Table(name = "sys_message_config")
@EqualsAndHashCode(callSuper = true)
public class SysMessageConfig extends TenantEntity {

    /**
     * 配置Key
     */
    @Schema(description = "配置Key")
    private String key;

    /**
     * 配置名称
     */
    @Schema(description = "配置名称")
    private String name;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private Enabled enabled;

    /**
     * 发送方式模板配置
     * <p>[{"sendWay":"发送方式 SYSTEM 系统信息、SMS 短信 EMAIL 邮箱 DINGDING ","sendKey":"发送标识","templates":[""],"protocol":"系统消息发送协议 WEBSOCKET 、MQTT "}],</p>
     */
    @Schema(description = "发送方式模板配置")
    private List<SendTemplate> sendTemplates;

    /**
     * 消息类型
     */
    @Schema(description = "消息类型")
    private String messageType;

    /**
     * 通知类型
     */
    @Schema(description = "通知类型")
    private NotificationType noticeType;

    /**
     * 失败重试
     * <p>{"strategy":"IGNORE 忽略 EXECUTE_IMMEDIATELY 立即执行 DELAYED_EXECUTION 延迟执行  ","count":1,"timeout":100000}</p>
     */
    @Schema(description = "失败重试")
    private RetryFailed retryFailed;

    /**
     * 发送目标
     * <p>[{"type":"用户类型 ORG 组织、GROUP 用户组、ROLE 角色、USER 指定用户、CUSTOM 自定义 ","key":"标识"}]/p>
     */
    @Schema(description = "发送目标")
    private List<SendTarget> sendTargets;
}
