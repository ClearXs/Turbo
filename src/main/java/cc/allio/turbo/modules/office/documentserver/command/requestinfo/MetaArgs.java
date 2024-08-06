package cc.allio.turbo.modules.office.documentserver.command.requestinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @see <a href="https://api.onlyoffice.com/editors/command/meta">meta</a>
 */
@Data
@Builder
public class MetaArgs {

    private Meta meta;

    @Data
    @AllArgsConstructor
    public static class Meta {

        private String title;
    }
}
