package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.entity.SysMessage;

/**
 * network is publish message methods.
 *
 * @author j.x
 * @date 2024/4/5 04:48
 * @since 0.1.1
 */
public interface Network {

    /**
     * 网络发送
     *
     * @param message 消息
     * @return true 成功 false 失败
     */
    boolean publish(SysMessage message);

    /**
     * 获取网络协议
     *
     * @return
     */
    String getProtocol();
}
