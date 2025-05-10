package cc.allio.turbo.modules.ai.api.mapper;

import cc.allio.turbo.modules.ai.entity.AIChat;
import cc.allio.turbo.modules.ai.api.vo.ConversationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AIChatMapper extends BaseMapper<AIChat> {

    IPage<ConversationVO> selectMineConversationPage(IPage<?> page, Long userId);
}
