package cc.allio.turbo.modules.system.entity;

import cc.allio.turbo.common.db.entity.TenantEntity;
import cc.allio.turbo.modules.system.constant.SendStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@TableName("sys_message_log")
@Schema(description = "系统消息日志")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageLog extends TenantEntity {

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
	 * 接收人
	 */
	@TableField("receiver")
	@Schema(description = "接收人")
	private Long receiver;

	/**
	 * 消息id
	 */
	@TableField("message_id")
	@Schema(description = "消息id")
	private Long messageId;

	/**
	 * 运行时消息变量
	 */
	@TableField("variables")
	@Schema(description = "运行时消息变量")
	private String variables;

	/**
	 * 消息发送状态
	 */
	@TableField("send_status")
	@Schema(description = "运行时消息变量")
	private SendStatus sendStatus;
}
