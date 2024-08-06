package cc.allio.turbo.modules.office.documentserver.callbacks.implementations;

import cc.allio.turbo.modules.office.documentserver.callbacks.Callback;
import cc.allio.turbo.modules.office.documentserver.callbacks.Status;
import cc.allio.turbo.modules.office.documentserver.command.CommandManager;
import cc.allio.turbo.modules.office.documentserver.command.requestinfo.ForceSaveArgs;
import cc.allio.turbo.modules.office.documentserver.vo.Action;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class EditCallback implements Callback {

    private final CommandManager commandManager;

    @Override
    public int handle(Long docId, Long fileId, String fileName, Track body) {
        int result = 0;

        List<Action> actions = body.getActions();
        if (CollectionUtils.isEmpty(actions)) {
            return result;
        }
        Action action = body.getActions().get(0);  // get the user ID who is editing the document
        if (cc.allio.turbo.modules.office.documentserver.models.enums.Action.edit.equals(action.getType())) {  // if this value is not equal to the user ID
            String user = action.getUserid();  // get user ID
            if (!body.getUsers().contains(user)) {  // if this user is not specified in the body
                String key = body.getKey();  // get document key
                try {
                    // create a command request to forcibly saves the document being edited without closing it
                    commandManager.forceSave().execute(key, ForceSaveArgs.builder().userdata(user).build());
                } catch (Exception ex) {
                    log.error("Failed to edit callback", ex);
                    result = 1;
                }
            }
        }
        return result;
    }

    @Override
    public int getStatus() {  // get document status
        return Status.EDITING.getCode();  // return status 1 - document is being edited
    }
}
