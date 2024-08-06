package cc.allio.turbo.modules.office.documentserver.configurers.wrappers;

import cc.allio.turbo.modules.office.documentserver.models.enums.Type;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DefaultEmbeddedWrapper {
    private Long docId;
    private Type type;
    private Long fileId;
    private String filename;
    private String filepath;
}
