package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.common.domain.Subscription;
import cc.allio.turbo.modules.ai.agent.runtime.ExecutionMode;
import cc.allio.turbo.modules.ai.api.convert.ConversationConvert;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.mapper.AIChatMapper;
import cc.allio.turbo.modules.ai.api.service.IAIChatService;
import cc.allio.turbo.modules.ai.api.service.IAIMessageService;
import cc.allio.turbo.modules.ai.api.vo.ConversationVO;
import cc.allio.turbo.modules.ai.driver.Driver;
import cc.allio.turbo.modules.ai.driver.Topics;
import cc.allio.turbo.modules.ai.driver.model.Input;
import cc.allio.turbo.modules.ai.driver.model.Options;
import cc.allio.turbo.modules.ai.model.ModelOptions;
import cc.allio.uno.core.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class AIChatServiceImpl extends TurboCrudServiceImpl<AIChatMapper, AIChat> implements IAIChatService {

    private final IAIMessageService messageService;

    @Override
    public void doOnSubscribe() throws Throwable {

        Driver<Input> driver = Driver.from(Input.class);

        // subscribe user chat input patterns and update each chat options data.
        // like as limit history or llm temperature etc...
        driver.subscribeOn(Topics.USER_CHAT_INPUT_PATTERNS)
                .observeMany()
                .map(Subscription::getDomain)
                .flatMap(Mono::justOrEmpty)
                .filter(input -> StringUtils.isNotBlank(input.getConversationId()))
                .subscribe(input -> {

                    String conversationId = input.getConversationId();
                    AIChat chat =
                            getOne(Wrappers.<AIChat>lambdaQuery().eq(AIChat::getId, Long.valueOf(conversationId)));

                    if (chat != null) {
                        // reset
                        ModelOptions modelOptions = input.getModelOptions();
                        chat.setModelOptions(modelOptions);

                        String agent = input.getAgent();
                        chat.setAgent(agent);

                        ExecutionMode executionMode = input.getExecutionMode();
                        chat.setExecutionMode(executionMode);

                        Options options = input.getOptions();
                        chat.setOptions(options);
                        updateById(chat);
                    }

                });

    }

    @Override
    public IPage<ConversationDTO> queryMineConversationsPage(IPage<AIChat> page, Long userId) {
        IPage<ConversationVO> conversationPage = getBaseMapper().selectMineConversationPage(page, userId);
        List<ConversationVO> records = conversationPage.getRecords();
        List<ConversationDTO> result = ConversationConvert.INSTANCE.vo2DTOList(records);
        IPage<ConversationDTO> conversationDTOPage =
                new Page<>(conversationPage.getCurrent(), conversationPage.getSize(), conversationPage.getTotal());
        conversationDTOPage.setRecords(result);
        return conversationDTOPage;
    }
}
