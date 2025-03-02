package cc.allio.turbo.modules.ai.api.controller;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.uno.core.bus.TopicKey;
import cc.allio.uno.core.util.id.IdGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RequestMapping("/ai/chat")
@Tag(name = "chat")
public class AIChatController extends GenericTurboCrudController<AIChat> {

    @Qualifier("Driver_Input")
    private Driver<Input> inputDriver;
    @Qualifier("Driver_Output")
    private Driver<Output> outputDriver;

    @PostMapping("/sse/{userId}")
    public SseEmitter chat(@PathVariable("userId") String userId, @RequestBody Input input) {
        SseEmitter sseEmitter = new SseEmitter();
        String sessionId = IdGenerator.defaultGenerator().toHex();
        input.setSessionId(sessionId);
        GeneralDomain<Input> domain = new GeneralDomain<>(input, inputDriver.getDomainEventBus());
        DomainEventContext context = new DomainEventContext(domain);

        TopicKey topicKey = Topics.USER_CHAT_INPUT.append(input.getSessionId());

        inputDriver.publishOn(topicKey, context)
                .thenMany(outputDriver.subscribeOn(Topics.USER_CHAT_OUTPUT.append(sessionId)).observeMany())
                .map(Subscription::getDomain)
                .flatMap(Mono::justOrEmpty)
                .subscribe(output -> {
                    try {
                        sseEmitter.send(output);
                    } catch (IOException e) {
                        // ignore
                    }
                });

        return sseEmitter;
    }
}
