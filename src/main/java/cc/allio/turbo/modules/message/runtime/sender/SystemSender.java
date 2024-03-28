package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.entity.SysMessage;

import java.util.Arrays;

/**
 * 系统消息发送
 *
 * @author j.x
 * @date 2024/3/29 00:26
 * @since 0.1.1
 */
public class SystemSender implements Sender {

	/**
	 * 消息发送协议
	 */
	private final Network[] networks;

	public SystemSender(Network[] networks) {
		this.networks = networks;
	}

	@Override
	public boolean send(SysMessage message) {
		return Arrays.stream(networks)
			.anyMatch(network -> network.publish(message));
	}
}
