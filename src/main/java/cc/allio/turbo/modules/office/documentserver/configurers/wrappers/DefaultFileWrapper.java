package cc.allio.turbo.modules.office.documentserver.configurers.wrappers;

import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.documentserver.models.enums.Type;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import cc.allio.turbo.modules.office.vo.DocUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class DefaultFileWrapper {
    private DocumentDescriptor doc;
    private Long fileId;
    private String filepath;
    private String filename;
    private Type type;
    private DocUser user;
    private String lang;
    private Action action;
    private String actionData;
    private Boolean canEdit;
    private Boolean isEnableDirectUrl;
}
