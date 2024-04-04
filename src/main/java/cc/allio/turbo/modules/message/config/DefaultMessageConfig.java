package cc.allio.turbo.modules.message.config;

import cc.allio.turbo.modules.message.constant.NotificationType;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;
import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import cc.allio.turbo.modules.message.template.DefaultMessageTemplate;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 默认消息配置
 *
 * @author j.x
 * @date 2024/3/29 00:13
 * @since 0.1.1
 */
@Getter
public class DefaultMessageConfig implements MessageConfig {

    private final Boolean enabled;
    private final String configKey;
    private final String messageType;
    private final NotificationType noticeType;
    private final List<Template> templates;
    private final RetryFailed retryFailed;
    private final List<SendTarget> sendTargets;
    private final SendModel sendModel;

    // DEFAULT
    private static final RetryFailed DEFAULT_RETRY_FAILED = new RetryFailed();
    private static final SendModel DEFAULT_SEND_MODEL = new SendModel();

    static {
        DEFAULT_RETRY_FAILED.setStrategy(RetryStrategy.IGNORE);
        DEFAULT_SEND_MODEL.setSendWay(SendWay.SYSTEM);
    }

    public DefaultMessageConfig(SysMessageConfig messageConfig) {
        // 构建发送模板设置
        this.configKey = messageConfig.getKey();
        this.messageType = messageConfig.getMessageType();
        this.noticeType = messageConfig.getNoticeType();
        this.retryFailed = Optional.ofNullable(messageConfig.getRetryFailed()).orElse(DEFAULT_RETRY_FAILED);
        this.sendTargets = Optional.ofNullable(messageConfig.getSendTargets()).orElse(Collections.emptyList());
        this.sendModel = Optional.ofNullable(messageConfig.getSendModel()).orElse(DEFAULT_SEND_MODEL);
        this.enabled = Optional.ofNullable(messageConfig.getEnabled()).orElse(Boolean.FALSE);
        this.templates = Lists.newArrayList();
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getKey() {
        return configKey;
    }

    @Override
    public List<SendTarget> getSendTarget() {
        return sendTargets;
    }

    /**
     * add system template
     *
     * @param sysMessageTemplate the sysMessageTemplate
     */
    public void addTemplate(SysMessageTemplate sysMessageTemplate) {
        Template template = new Template(sendModel.getSendWay(), sendModel.getSendKey(), new DefaultMessageTemplate(sysMessageTemplate), sendModel.getProtocols());
        this.templates.add(template);
    }
}
