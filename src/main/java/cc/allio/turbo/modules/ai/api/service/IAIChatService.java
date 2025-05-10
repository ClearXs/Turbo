package cc.allio.turbo.modules.ai.api.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.entity.AIChat;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface IAIChatService extends ITurboCrudService<AIChat> {

    IPage<ConversationDTO> queryMineConversationsPage(IPage<AIChat> page, Long userId);
}
