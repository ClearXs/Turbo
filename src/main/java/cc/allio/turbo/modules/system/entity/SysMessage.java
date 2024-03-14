package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.system.constant.MessageSource;
import cc.allio.turbo.modules.system.constant.MessageStatus;
import cc.allio.turbo.modules.system.constant.NotificationType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@TableName("sys_message")
@Schema(description = "系统消息")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessage extends TenantEntity {

    /**
     * 消息标题
     */
    @TableField("title")
    @Schema(description = "标题")
    private String title;

    /**
     * 副标题
     */
    @TableField("subtitle")
    @Schema(description = "副标题")
    private String subtitle;

    /**
     * 消息内容
     */
    @TableField("content")
    @Schema(description = "消息内容")
    private String content;

    /**
     * 消息来源
     */
    @TableField("message_source")
    @Schema(description = "消息来源")
    private MessageSource messageSource;

    /**
     * 消息状态
     */
    @TableField("message_status")
    @Schema(description = "消息来源")
    private MessageStatus messageStatus;

    /**
     * 消息类型
     */
    @TableField("message_type")
    @Schema(description = "消息类型")
    private String messageType;

    /**
     * 发送人
     */
    @TableField("send_user")
    @Schema(description = "发送人")
    private Long sendUser;

    /**
     * 发送时间
     */
    @TableField("send_time")
    @Schema(description = "发送时间")
    private Date sendTime;

    /**
     * pc url
     */
    @TableField("pc_url")
    @Schema(description = "pc url")
    private String pcUrl;

    /**
     * app url
     */
    @TableField("app_url")
    @Schema(description = "app url")
    private String appUrl;

    /**
     * 接收人
     */
    @TableField("receiver")
    @Schema(description = "接收人")
    private Long receiver;

    /**
     * 消息配置Key
     */
    @TableField("config_key")
    @Schema(description = "消息配置Key")
    private String configKey;

    /**
     * 消息模板Key
     */
    @TableField("config_key")
    @Schema(description = "消息配置Key")
    private String templateKey;

    /**
     * 通知类型
     */
    @TableField("action")
    @Schema(description = "通知类型")
    private NotificationType action;
}
