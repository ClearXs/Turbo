package cc.allio.turbo.modules.office.documentserver.configurers.wrappers;

import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.vo.DocUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DefaultCustomizationWrapper {
    private Action action;
    private DocUser user;
}
