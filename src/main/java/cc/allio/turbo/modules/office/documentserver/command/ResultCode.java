package cc.allio.turbo.modules.office.documentserver.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://api.onlyoffice.com/editors/command/">command request result</a>
 *
 * @author j.x
 * @date 2024/5/13 19:11
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
public enum ResultCode {

    //  No errors.
    noError(0),
    // Document key is missing or no document with such key could be found.
    notFoundDocKey(1),
    // Callback url not correct.
    callbackUrlCorrect(2),
    // Internal server error.
    intervalServerError(3),
    // No changes were applied to the document before the forcesave command was received.
    noChange(4),
    // Command not correct.
    commandCorrect(5),
    // Invalid token.
    invalidToken(6);

    private final Integer code;

}
