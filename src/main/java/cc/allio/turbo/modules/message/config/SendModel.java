package cc.allio.turbo.modules.message.config;

import lombok.Data;

/**
 * 发送与模板配置
 */
@Data
public class SendModel {

    /**
     * 发送方式
     * <P>SYSTEM 系统信息、SMS 短信 EMAIL 邮箱 DINGDING</P>
     */
    private SendWay sendWay;

    /**
     * 发送标识 用于SMS、EMAIL等
     */
    private String sendKey;

    /**
     * 消息推送的网络集合
     * <p>WEBSOCKET、MQTT</p>
     */
    private Protocol[] protocols;
}
