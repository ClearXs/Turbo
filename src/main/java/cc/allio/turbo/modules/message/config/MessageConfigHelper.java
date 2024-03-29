package cc.allio.turbo.modules.message.config;

import cc.allio.turbo.modules.message.entity.SysMessageConfig;
import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import cc.allio.turbo.modules.message.service.ISysMessageConfigService;
import cc.allio.turbo.modules.message.service.ISysMessageTemplateService;
import cc.allio.turbo.modules.message.template.DefaultMessageTemplate;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class MessageConfigHelper {

    private final ISysMessageConfigService messageConfigService;
    private final ISysMessageTemplateService templateService;

    /**
     * base on configKey return {@link MessageConfig}
     * <p>query on cache obtain {@link SysMessageConfig} then combination {@link MessageConfig}</p>
     *
     * @param configKey configKey
     * @return MessageConfig
     */
    public MessageConfig getConfig(String configKey) {
        SysMessageConfig messageConfig = messageConfigService.get(configKey, SysMessageConfig.class);
        List<SendTemplate> sendTemplates = messageConfig.getSendTemplates();
        List<Template> templates = sendTemplates.stream()
                .flatMap(sendTemplate ->
                        Arrays.stream(sendTemplate.getTemplates())
                                .map(templateKey -> {
                                    SysMessageTemplate template = templateService.getOne(Wrappers.<SysMessageTemplate>lambdaQuery().eq(SysMessageTemplate::getKey, templateKey));
                                    return new Template(sendTemplate.getSendWay(), sendTemplate.getSendKey(), new DefaultMessageTemplate(template), sendTemplate.getProtocols());
                                }))
                .toList();
        return new DefaultMessageConfiguration(messageConfig, templates);
    }
}
