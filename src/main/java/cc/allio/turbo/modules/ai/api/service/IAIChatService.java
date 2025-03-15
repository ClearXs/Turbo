package cc.allio.turbo.modules.ai.api.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.entity.AIMessage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface IAIChatService extends ITurboCrudService<AIChat> {

    IPage<ConversationDTO> queryMineConversationsPage(IPage<AIChat> page, Long userId);
}
