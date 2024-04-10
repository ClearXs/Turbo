package cc.allio.turbo.common.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * definition app, like pc and app and mini program etc...
 *
 * @author j.x
 * @date 2024/4/10 17:13
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public enum App {

    PC("pc"),
    APP("app"),
    MINI_PROGRAM("mini");

    private final String value;

    /**
     * compare enum value and specific value.
     *
     * @param value the value
     * @return the {@link App}
     */
    public static App ofValue(String value) {
        for (App app : values()) {
            if (app.getValue().equals(value)) {
                return app;
            }
        }
        return null;
    }
}


