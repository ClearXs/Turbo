package cc.allio.turbo.modules.message.config;

import cc.allio.turbo.modules.message.constant.NotificationType;

import java.util.List;

/**
 * 消息配置接口，
 *
 * @author jiangwei
 * @date 2022/12/2 20:45
 * @since 2.9.0-RELEASE
 */
public interface MessageConfig {

    /**
     * 获取Key
     *
     * @return
     */
    String getKey();

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 获取通知类型
     *
     * @return 通知类型
     */
    NotificationType getNoticeType();

    /**
     * 获取发送与模板配置
     *
     * @return 配置List结构
     */
    List<Template> getTemplates();

    /**
     * 获取失败重试配置
     *
     * @return 失败重试实体
     */
    RetryFailed getRetryFailed();

    /**
     * 获取发送目标配置
     *
     * @return List
     */
    List<SendTarget> getSendTarget();
}
