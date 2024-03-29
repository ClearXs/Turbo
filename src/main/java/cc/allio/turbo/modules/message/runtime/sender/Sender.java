package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.entity.SysMessage;

import java.util.Arrays;

/**
 * 消息发送器
 *
 * @author j.x
 * @date 2024/3/29 00:25
 * @since 0.1.1
 */
public interface Sender {

	/**
	 * 消息发送
	 *
	 * @param message 消息内容
	 * @return true 消息发送成功，false
	 */
	boolean send(SysMessage message);

	/**
	 * 批量发送
	 *
	 * @param messages
	 */
	default void batchSend(SysMessage... messages) {
		Arrays.stream(messages).forEach(this::send);
	}
}
