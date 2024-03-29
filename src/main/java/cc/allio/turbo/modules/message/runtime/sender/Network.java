package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.entity.SysMessage;

/**
 * 消息发送网络
 *
 * @author jiangwei
 * @date 2022/12/6 11:27
 * @since 2.9.0-RELEASE
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
