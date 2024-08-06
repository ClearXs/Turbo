package cc.allio.turbo.modules.office.documentserver.callbacks.implementations;

import cc.allio.turbo.modules.office.documentserver.callbacks.Callback;
import cc.allio.turbo.modules.office.documentserver.callbacks.Status;
import cc.allio.turbo.modules.office.documentserver.vo.Track;

/**
 * handle callback status {@link Status#CORRUPTED}
 *
 * @author j.x
 * @date 2024/5/12 09:43
 * @since 0.0.1
 */
public class CorruptedCallback implements Callback {

    private final SaveCallback saveCallback;

    public CorruptedCallback(SaveCallback saveCallback) {
        this.saveCallback = saveCallback;
    }

    @Override
    public int handle(Long docId, Long fileId, String fileName, Track body) {
        return saveCallback.handle(docId, fileId, fileName, body);
    }

    @Override
    public int getStatus() {
        return Status.CORRUPTED.getCode();
    }
}
