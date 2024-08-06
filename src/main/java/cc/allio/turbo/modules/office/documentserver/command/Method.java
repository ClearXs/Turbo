package cc.allio.turbo.modules.office.documentserver.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://api.onlyoffice.com/editors/command/">onlyoffice command request method</a>
 *
 * @author j.x
 * @date 2024/5/13 19:06
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
public enum Method {

    deleteForgotten("deleteForgotten"),
    // drop(kickout) specifies users
    drop("drop"),
    forcesave("forcesave"),
    getForgotten("getForgotten"),
    getForgottenList("getForgottenList"),
    info("info"),
    license("license"),
    meta("meta"),
    version("version");

    private final String name;
}
