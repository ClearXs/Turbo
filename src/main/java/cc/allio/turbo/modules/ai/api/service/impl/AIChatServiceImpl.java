package cc.allio.turbo.modules.ai.api.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.ai.api.convert.ConversationConvert;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.mapper.AIChatMapper;
import cc.allio.turbo.modules.ai.api.service.IAIChatService;
import cc.allio.turbo.modules.ai.api.vo.ConversationVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AIChatServiceImpl extends TurboCrudServiceImpl<AIChatMapper, AIChat> implements IAIChatService {

    @Override
    public IPage<ConversationDTO> queryMineConversationPage(IPage<AIChat> page, Long userId) {
        IPage<ConversationVO> conversationPage = getBaseMapper().selectMineConversationPage(page, userId);
        List<ConversationVO> records = conversationPage.getRecords();
        List<ConversationDTO> result = ConversationConvert.INSTANCE.vo2DTOList(records);
        IPage<ConversationDTO> conversationDTOPage =
                new Page<>(conversationPage.getCurrent(), conversationPage.getSize(), conversationPage.getTotal());
        conversationDTOPage.setRecords(result);
        return conversationDTOPage;
    }
}
