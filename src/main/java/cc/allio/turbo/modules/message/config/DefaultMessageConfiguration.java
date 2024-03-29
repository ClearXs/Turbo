package cc.allio.turbo.modules.message.config;

import cc.allio.turbo.modules.message.constant.NotificationType;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;

import java.util.List;

/**
 * 默认消息配置
 *
 * @author j.x
 * @date 2024/3/29 00:13
 * @since 0.1.1
 */
public class DefaultMessageConfiguration implements MessageConfig {

    private final String configKey;
    private final String messageType;
    private final NotificationType noticeType;
    private final List<Template> templates;
    private final RetryFailed retryFailed;
    private final List<SendTarget> sendTargets;

    // DEFAULT
    private static final RetryFailed DEFAULT_RETRY_FAILED = new RetryFailed();

    static {
        DEFAULT_RETRY_FAILED.setStrategy(RetryStrategy.IGNORE);
    }

    public DefaultMessageConfiguration(SysMessageConfig messageConfig, List<Template> templates) {
        // 构建发送模板设置
        this.configKey = messageConfig.getKey();
        this.messageType = messageConfig.getMessageType();
        this.noticeType = messageConfig.getNoticeType();
        this.templates = templates;
        this.retryFailed = messageConfig.getRetryFailed();
        this.sendTargets = messageConfig.getSendTargets();
    }

    @Override
    public String getKey() {
        return configKey;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public NotificationType getNoticeType() {
        return noticeType;
    }

    @Override
    public List<Template> getTemplates() {
        return templates;
    }

    @Override
    public RetryFailed getRetryFailed() {
        return retryFailed;
    }

    @Override
    public List<SendTarget> getSendTarget() {
        return sendTargets;
    }

}
