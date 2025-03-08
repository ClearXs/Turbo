package cc.allio.turbo.modules.ai.api.convert;

import cc.allio.turbo.common.convert.Convert;
import cc.allio.turbo.modules.ai.api.dto.ConversationDTO;
import cc.allio.turbo.modules.ai.api.entity.AIChat;
import cc.allio.turbo.modules.ai.api.vo.ConversationVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConversationConvert extends Convert<AIChat, ConversationVO, ConversationDTO> {

    ConversationConvert INSTANCE = Mappers.getMapper(ConversationConvert.class);
}
