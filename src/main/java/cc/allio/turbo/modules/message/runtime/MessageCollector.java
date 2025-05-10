package cc.allio.turbo.modules.message.runtime;

import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.modules.message.config.*;
import cc.allio.turbo.modules.message.enums.SendStatus;
import cc.allio.turbo.modules.message.enums.Source;
import cc.allio.turbo.modules.message.enums.Status;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.entity.SysMessageConfig;
import cc.allio.turbo.modules.message.entity.SysMessageLog;
import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import cc.allio.turbo.modules.message.runtime.sender.Sender;
import cc.allio.turbo.modules.message.runtime.sender.Senders;
import cc.allio.turbo.modules.message.runtime.targeter.Targets;
import cc.allio.turbo.modules.message.service.ISysMessageConfigService;
import cc.allio.turbo.modules.message.service.ISysMessageLogService;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import cc.allio.turbo.modules.message.service.ISysMessageTemplateService;
import cc.allio.turbo.modules.message.template.Extension;
import cc.allio.turbo.modules.message.template.MessageTemplate;
import cc.allio.turbo.modules.message.template.TemporaryTemplate;
import cc.allio.uno.core.exception.Trys;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 消息收集器
 *
 * @author j.x
 * @date 2024/3/29 00:37
 * @since 0.1.1
 */
@AllArgsConstructor
public class MessageCollector implements SourceCollector<ReceiveMetadata> {

    private final ISysMessageConfigService messageConfigService;
    private final ISysMessageTemplateService templateService;
    private final ISysMessageService messageService;
    private final ISysMessageLogService messageLogService;

    @Override
    public void collect(ReceiveMetadata element) {
        Mono.just(element).flatMapMany(this::disposeMessage)
                // 保存消息数据
                .flatMap(gk ->
                        gk.flatMap(messages -> {
                            Template template = gk.key();
                            // 创建Sender对象
                            Sender sender = Senders.create(template);
                            return Flux.fromIterable(messages)
                                    .map(runtimeMessage -> {
                                        // 存储于历史数据
                                        SysMessage message = runtimeMessage.getMessage();
                                        boolean send = Optional.ofNullable(sender).map(s -> s.send(message)).orElse(false);
                                        SysMessageLog messageLog = Objects.requireNonNull(BeanUtils.copy(message, SysMessageLog.class));
                                        messageLog.setMessageId(message.getId());
                                        messageLog.setVariables(runtimeMessage.getMessageTemplate().getRuntimeVariables().getJsonVariables());
                                        messageLog.setSendStatus(SendStatus.binaryCreate(send));
                                        return messageLog;
                                    })
                                    .collectList()
                                    .doOnNext(messageLogService::saveBatch)
                                    .thenMany(Flux.fromIterable(messages));
                        }))
                .map(RuntimeMessage::getMessage)
                .collectList()
                .subscribe(messageService::saveBatch);
    }

    /**
     * 获取消息分组 根据消息的模板将消息进行分组
     *
     * @param receive 接收元数据
     * @throws UnsupportedOperationException when {@link MessageConfig} disable
     */
    private Flux<GroupedFlux<Template, List<RuntimeMessage>>> disposeMessage(ReceiveMetadata receive) {
        String configKey = receive.getConfigKey();
        MessageConfig messageConfig = getConfig(configKey, receive);
        if (Boolean.FALSE.equals(messageConfig.isEnabled())) {
            throw new UnsupportedOperationException(String.format("UnEnable Message Configuration %s", messageConfig.getKey()));
        }
        Map<String, Object> variables = receive.getVariables();
        // 构建目标发送人集合
        List<SendTarget> sendTarget = messageConfig.getSendTarget();
        List<Long> targets =
                sendTarget.stream()
                        .flatMap(target -> Targets.getTargetUser(target, new RuntimeVariable(receive.getVariables())).stream())
                        .toList();
        List<Template> templates = messageConfig.getTemplates();
        return Flux.fromIterable(templates)
                .groupBy(
                        t -> t,
                        template -> {
                            MessageTemplate messageTemplate = template.getMessageTemplate();
                            RuntimeVariable runtimeVariable = new RuntimeVariable(messageTemplate.getVariable(), receive.getVariables());
                            RuntimeMessageTemplate runtimeMessageTemplate = new RuntimeMessageTemplate(messageTemplate, runtimeVariable);
                            Extension extension = runtimeMessageTemplate.getExtension();
                            return targets.stream()
                                    .map(target -> {
                                        SysMessage sysMessage = new SysMessage();
                                        sysMessage.setConfigKey(configKey);
                                        sysMessage.setTemplateKey(messageTemplate.getKey());
                                        sysMessage.setMessageType(messageConfig.getMessageType());
                                        sysMessage.setAction(messageConfig.getNoticeType());
                                        sysMessage.setMessageStatus(Status.UNREAD);
                                        sysMessage.setMessageSource(Source.SYSTEM);
                                        // 发送人、接收人
                                        String userId = AuthUtil.getUserId();
                                        sysMessage.setSendUser(Trys.onContinue(() -> Long.valueOf(userId)));
                                        sysMessage.setSendTime(DateUtil.parse(String.valueOf(variables
                                                .getOrDefault("sendTime", DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME))), DateUtil.PATTERN_DATETIME));
                                        sysMessage.setReceiver(target);
                                        // 消息内容
                                        sysMessage.setContent(runtimeMessageTemplate.getContentText().thenText());
                                        sysMessage.setTitle(String.valueOf(variables.getOrDefault("title", runtimeMessageTemplate.getTitleTxt().thenText())));
                                        sysMessage.setSubtitle(String.valueOf(variables.getOrDefault("subtitle", "")));
                                        sysMessage.setAppUrl(runtimeMessageTemplate.getRuntimeText(extension.getAppUrl()).thenText());
                                        sysMessage.setPcUrl(runtimeMessageTemplate.getRuntimeText(extension.getPcUrl()).thenText());
                                        return new RuntimeMessage()
                                                .setMessage(sysMessage)
                                                .setMessageConfiguration(messageConfig)
                                                .setReceive(receive)
                                                .setMessageTemplate(runtimeMessageTemplate);
                                    })
                                    .toList();
                        });
    }

    /**
     * base on configKey return {@link MessageConfig}
     * <p>query on cache obtain {@link SysMessageConfig} then combination {@link MessageConfig}</p>
     *
     * @param configKey configKey
     * @return MessageConfig
     */
    MessageConfig getConfig(String configKey, ReceiveMetadata receiveMetadata) {
        SysMessageConfig messageConfig = messageConfigService.getOne(Wrappers.lambdaQuery(SysMessageConfig.class).eq(SysMessageConfig::getKey, configKey));
        if (messageConfig == null) {
            throw new NullPointerException(String.format("According to %s config key not found any config", configKey));
        }
        List<Long> defaultTemplateIds = messageConfig.getDefaultTemplateIds();
        // find default template info
        List<SysMessageTemplate> messageTemplates = templateService.list(Wrappers.lambdaQuery(SysMessageTemplate.class).in(SysMessageTemplate::getId, defaultTemplateIds));
        // temporary template
        TemporaryTemplate temporary = receiveMetadata.getTemporary();
        if (temporary != null) {
            SysMessageTemplate temporaryTemplate = new SysMessageTemplate();
            temporaryTemplate.setTitle(temporary.getTitle());
            temporaryTemplate.setSubtitle(temporary.getSubtitle());
            temporaryTemplate.setTemplate(temporary.getTemplate());
            temporaryTemplate.setExtension(temporary.getExtension());
            temporaryTemplate.setVariables(temporary.getVariableList());
            messageTemplates.add(temporaryTemplate);
        }
        DefaultMessageConfig messageConfiguration = new DefaultMessageConfig(messageConfig);

        // add template
        for (SysMessageTemplate messageTemplate : messageTemplates) {
            messageConfiguration.addTemplate(messageTemplate);
        }
        return messageConfiguration;
    }
}
