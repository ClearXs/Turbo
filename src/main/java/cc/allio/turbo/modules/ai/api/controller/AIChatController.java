package cc.allio.turbo.modules.ai.api.controller;

import cc.allio.turbo.common.domain.DomainEventContext;
import cc.allio.turbo.common.domain.GeneralDomain;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.api.entity.AIChatSession;
import cc.allio.turbo.modules.ai.api.service.IAIChatService;
import cc.allio.turbo.modules.ai.api.service.IAIChatSessionService;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Output;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.core.bus.TopicKey;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RequestMapping("/ai/chat")
@Tag(name = "chat")
@RestController
public class AIChatController extends TurboCrudController<AIChat,AIChat, IAIChatService> {

    @Qualifier("Driver_Input")
    private Driver<Input> inputDriver;
    @Qualifier("Driver_Output")
    private Driver<Output> outputDriver;

    @Autowired
    private IAIChatSessionService chatSessionService;

    @PostMapping("/sse/{conversationId}")
    public SseEmitter chat(@PathVariable("conversationId") Long conversationId, TurboUser user, @RequestBody Input input) {
        SseEmitter sseEmitter = new SseEmitter();

        // save the new session
        AIChatSession chatSession = new AIChatSession();
        chatSession.setChatId(conversationId);
        chatSession.setUserId(user.getUserId());
        chatSessionService.save(chatSession);

        input.setConversationId(String.valueOf(conversationId));

        String sessionId = chatSession.getId();
        input.setSessionId(sessionId);

        GeneralDomain<Input> domain = new GeneralDomain<>(input, inputDriver.getDomainEventBus());
        DomainEventContext context = new DomainEventContext(domain);

        TopicKey inputTopic = Topics.USER_CHAT_INPUT.append(conversationId).append(sessionId);
        inputDriver.publishOn(inputTopic, context).subscribe();

        TopicKey outputTopic = Topics.USER_CHAT_OUTPUT.append(conversationId).append(sessionId);
        Disposable disposable =
                outputDriver.subscribeOn(outputTopic).observeMany()
                        .map(Subscription::getDomain)
                        .flatMap(Mono::justOrEmpty)
                        .subscribe(output -> {
                            try {
                                sseEmitter.send(output);
                            } catch (IOException e) {
                                // ignore
                            }
                        });

        sseEmitter.onError(err -> {
            disposable.dispose();
            sseEmitter.complete();
        });

        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            disposable.dispose();
        });

        return sseEmitter;
    }

    @PostMapping("/newConversation")
    public R<AIChat> newConversation(TurboUser user) {
        AIChat chat = new AIChat();
        Long userId = user.getUserId();
        chat.setUserId(userId);
        getService().save(chat);
        return R.ok(chat);
    }

    @PostMapping("/mineConversation")
    public R<IPage<ConversationDTO>> mineConversation(TurboUser user, @RequestBody QueryParam<AIChat> params) {
        IPage<ConversationDTO> page = getService().queryMineConversationPage(params.getPage(), user.getUserId());
        return R.ok(page);
    }
}
