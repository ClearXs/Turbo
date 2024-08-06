package cc.allio.turbo.modules.office.documentserver.command.requestinfo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @see <a href="https://api.onlyoffice.com/zh/editors/command/drop"></a>
 */
@Data
@Builder
public class DropArgs {

    private List<String> users;
}
