package cc.allio.turbo.modules.office.documentserver.callbacks.implementations;

import cc.allio.turbo.modules.office.documentserver.callbacks.Callback;
import cc.allio.turbo.modules.office.documentserver.callbacks.Status;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import cc.allio.turbo.modules.office.service.IDocService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SaveCallback implements Callback {

    private final IDocService docService;

    @Override
    public int handle(Long docId, Long fileId, String fileName, Track body) {  // handle the callback when the saving request is performed
        int result = 0;
        try {
            docService.newVersion(docId, fileName, body);  // file saving process
        } catch (Exception ex) {
            log.error("Failed to saves callback", ex);
            result = 1;
        }
        return result;
    }

    @Override
    public int getStatus() {  // get document status
        return Status.SAVE.getCode();  // return status 2 - document is ready for saving
    }
}
