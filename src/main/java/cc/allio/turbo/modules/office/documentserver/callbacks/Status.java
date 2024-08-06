package cc.allio.turbo.modules.office.documentserver.callbacks;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://api.onlyoffice.com/editors/callback">onlyoffice callback document status</a>
 *
 * @author j.x
 * @date 2024/5/11 16:32
 * @since 0.0.1
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Status {

    EDITING(1),  // 1 - document is being edited
    SAVE(2),  // 2 - document is ready for saving
    CORRUPTED(3),  // 3 - document saving error has occurred
    NO_CHANGED(4), // 4 - document is closed with no changes,
    MUST_FORCE_SAVE(6),  // 6 - document is being edited, but the current document state is saved
    CORRUPTED_FORCE_SAVE(7);  // 7 - error has occurred while force saving the document
    @JsonValue
    private final int code;
}
