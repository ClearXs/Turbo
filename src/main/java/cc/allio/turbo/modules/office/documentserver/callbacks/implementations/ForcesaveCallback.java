package cc.allio.turbo.modules.office.documentserver.callbacks.implementations;

import cc.allio.turbo.modules.office.documentserver.callbacks.Callback;
import cc.allio.turbo.modules.office.documentserver.callbacks.Status;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import cc.allio.turbo.modules.office.service.IDocService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ForcesaveCallback implements Callback {

    private final IDocService docService;

    @Override
    public int handle(Long docId, Long fileId, String fileName, Track body) {
        // handle the callback when the force saving request is performed
        int result = 0;
        try {
            // file force saving process
            docService.forceSave(docId, fileName, body);
        } catch (Exception ex) {
            log.error("Failed to force saves callback", ex);
            result = 1;
        }
        return result;
    }

    @Override
    public int getStatus() {
        // get document status
        // return status 6 - document is being edited, but the current document state is saved
        return Status.MUST_FORCE_SAVE.getCode();
    }
}
