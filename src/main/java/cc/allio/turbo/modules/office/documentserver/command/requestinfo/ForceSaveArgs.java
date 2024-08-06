package cc.allio.turbo.modules.office.documentserver.command.requestinfo;

import lombok.Builder;
import lombok.Data;

/**
 * @see <a href="https://api.onlyoffice.com/zh/editors/command/forcesave">forcesave</a>
 */
@Data
@Builder
public class ForceSaveArgs {

    private String userdata;
}
