package cc.allio.turbo.modules.office.documentserver.callbacks;

import cc.allio.turbo.modules.office.documentserver.vo.Track;
import org.springframework.beans.factory.annotation.Autowired;

// specify the callback handler functions
public interface Callback {

    /**
     *  handle the callback
     */
    int handle(Long docId, Long fileId, String fileName, Track body);

    int getStatus();  // get document status

    @Autowired
    default void selfRegistration(CallbackHandler callbackHandler) {  // register a callback handler
        callbackHandler.register(getStatus(), this);
    }
}
