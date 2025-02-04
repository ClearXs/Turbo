package cc.allio.turbo.modules.message;

import cc.allio.turbo.modules.message.runtime.MessageCollector;
import cc.allio.turbo.modules.message.runtime.MessageEndpoint;
import cc.allio.turbo.modules.message.runtime.MessageSourceConverter;
import cc.allio.turbo.modules.message.service.ISysMessageConfigService;
import cc.allio.turbo.modules.message.service.ISysMessageLogService;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import cc.allio.turbo.modules.message.service.ISysMessageTemplateService;
import cc.allio.uno.core.metadata.endpoint.source.SinkSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {

    @Bean
    public SinkSource sinkSource() {
        return new SinkSource();
    }

    @Bean
    public MessageEndpoint messageEndpoint(ISysMessageService messageService,
                                           ISysMessageLogService messageLogService,
                                           ISysMessageConfigService messageConfigService,
                                           ISysMessageTemplateService templateService,
                                           SinkSource sinkSource) {
        MessageSourceConverter messageSourceConverter = new MessageSourceConverter();
        MessageCollector messageCollector = new MessageCollector(messageConfigService, templateService, messageService, messageLogService);
        MessageEndpoint messageEndpoint = new MessageEndpoint(messageSourceConverter, messageCollector);
        messageEndpoint.registerSource(sinkSource);
        return messageEndpoint;
    }
}