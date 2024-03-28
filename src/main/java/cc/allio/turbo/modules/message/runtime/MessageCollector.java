package cc.allio.turbo.modules.message.runtime;

import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.modules.message.config.MessageConfig;
import cc.allio.turbo.modules.message.config.MessageConfigHelper;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Template;
import cc.allio.turbo.modules.message.constant.SendStatus;
import cc.allio.turbo.modules.message.constant.Status;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.entity.SysMessageLog;
import cc.allio.turbo.modules.message.runtime.sender.Sender;
import cc.allio.turbo.modules.message.runtime.sender.Senders;
import cc.allio.turbo.modules.message.runtime.targeter.Targets;
import cc.allio.turbo.modules.message.service.ISysMessageLogService;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import cc.allio.turbo.modules.message.template.Extension;
import cc.allio.turbo.modules.message.template.MessageTemplate;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.DateUtil;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消息收集器
 *
 * @author j.x
 * @date 2024/3/29 00:37
 * @since 0.1.1
 */
@AllArgsConstructor
public class MessageCollector implements SourceCollector<ReceiveMetadata> {

    private final MessageConfigHelper configHelper;
    private final ISysMessageService messageRepository;
    private final ISysMessageLogService messageLogRepository;

    @Override
    public void collect(ReceiveMetadata element) {
        Mono.just(element)
                .flatMapMany(receive -> disposeMessage(receive, configHelper))
                // 保存消息数据
                .flatMap(gk ->
                        gk.flatMap(messages -> {
                            Template template = gk.key();

                            // 创建Sender对象
                            Sender sender = Senders.create(template);
                            if (sender == null) {
                                return Flux.empty();
                            }
                            return Flux.fromIterable(messages)
                                    .map(runtimeMessage -> {
                                        // 存储于历史数据
                                        SysMessage message = runtimeMessage.getMessage();
                                        boolean send = sender.send(message);
                                        SysMessageLog messageLog = Objects.requireNonNull(BeanUtils.copy(message, SysMessageLog.class));
                                        messageLog.setMessageId(message.getId());
                                        messageLog.setVariables(runtimeMessage.getMessageTemplate().getRuntimeVariables().getJsonVariables());
                                        messageLog.setSendStatus(SendStatus.binaryCreate(send));
                                        return messageLog;
                                    })
                                    .collectList()
                                    .doOnNext(messageLogRepository::saveBatch)
                                    .thenMany(Flux.fromIterable(messages));
                        }))
                .map(RuntimeMessage::getMessage)
                .collectList()
                .subscribe(messageRepository::saveBatch);
    }

    /**
     * 获取消息分组 根据消息的模板将消息进行分组
     *
     * @param receive      接收元数据
     * @param configHelper the helper
     */
    private Flux<GroupedFlux<Template, List<RuntimeMessage>>> disposeMessage(ReceiveMetadata receive, MessageConfigHelper configHelper) {
        String configKey = receive.getConfigKey();
        MessageConfig messageConfiguration = configHelper.getConfig(configKey);
        Map<String, Object> variables = receive.getVariables();
        // 构建目标发送人集合
        List<SendTarget> sendTarget = messageConfiguration.getSendTarget();
        List<Long> targets =
                sendTarget.stream()
                        .flatMap(target -> Targets.getTargetUser(target, new RuntimeVariable(receive.getVariables())).stream())
                        .toList();
        // 构建BladeMessage。
        // 按照发送方式分组（相同的SendWay，构造参数不一致，也是不一样的发送方式，所以这个地方需要以Template作为Key，而不是Template.getSendWay）
        return Flux.fromIterable(messageConfiguration.getTemplates())
                .groupBy(template -> template, template -> {
                    MessageTemplate messageTemplate = template.getMessageTemplate();
                    RuntimeVariable runtimeVariable = new RuntimeVariable(messageTemplate.getVariable(), receive.getVariables());
                    RuntimeMessageTemplate runtimeMessageTemplate = new RuntimeMessageTemplate(messageTemplate, runtimeVariable);
                    Extension extension = runtimeMessageTemplate.getExtension();
                    return targets.stream()
                            .map(target -> {
                                SysMessage sysMessage = new SysMessage();
                                sysMessage.setConfigKey(configKey);
                                sysMessage.setTemplateKey(messageTemplate.getKey());
                                sysMessage.setMessageType(messageConfiguration.getMessageType());
                                sysMessage.setAction(messageConfiguration.getNoticeType());
                                sysMessage.setMessageStatus(Status.UN_READABLE);
                                // 发送人、接收人
                                sysMessage.setSendUser(AuthUtil.getCurrentUserId());
                                sysMessage.setSendTime(DateUtil.parse(String.valueOf(variables
                                        .getOrDefault("sendTime", DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATETIME))), DateUtil.PATTERN_DATETIME));
                                sysMessage.setReceiver(target);
                                // 消息内容
                                sysMessage.setContent(runtimeMessageTemplate.getContentText().runThenText());
                                sysMessage.setTitle(String.valueOf(variables.getOrDefault("title", runtimeMessageTemplate.getTitleTxt().runThenText())));
                                sysMessage.setSubtitle(String.valueOf(variables.getOrDefault("subtitle", "")));
                                sysMessage.setAppUrl(runtimeMessageTemplate.getRuntimeText(extension.getAppUrl()).runThenText());
                                sysMessage.setPcUrl(runtimeMessageTemplate.getRuntimeText(extension.getPcUrl()).runThenText());
                                return new RuntimeMessage()
                                        .setMessage(sysMessage)
                                        .setMessageConfiguration(messageConfiguration)
                                        .setReceive(receive)
                                        .setMessageTemplate(runtimeMessageTemplate);
                            })
                            .collect(Collectors.toList());
                });
    }
}
