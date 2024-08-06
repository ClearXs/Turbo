package cc.allio.turbo.modules.office.documentserver.configurers.mapper;

import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.CommentGroup;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.Permission;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import cc.allio.turbo.modules.office.vo.DocPermission;
import cc.allio.turbo.modules.office.vo.DocUser;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * map to {@link Permission}
 *
 * @author j.x
 * @date 2024/5/9 22:39
 * @since 0.0.1
 */
@AllArgsConstructor
public class PermissionMapper implements Mapper<DefaultFileWrapper, Permission> {

    private final FileUtility fileUtility;

    @Override
    public Permission toModel(DefaultFileWrapper wrapper) {
        // get the action parameter from the file wrapper
        Action action = wrapper.getAction();
        // get the DocUser parameter from file wrapper
        DocUser user = wrapper.getUser();
        // set permission from user permission
        String fileExt = fileUtility.getFileExtension(wrapper.getFilename());
        boolean canEdit = fileUtility.getEditedExts().contains(fileExt);
        if ((!canEdit && action.equals(Action.edit) || action.equals(Action.fillForms)) && fileUtility
                .getFillExts().contains(fileExt)) {
            canEdit = true;
            wrapper.setAction(Action.fillForms);
        }
        wrapper.setCanEdit(canEdit);

        return settingPermission(user.getPermission(), user.getPermissionGroup(), action, canEdit);
    }

    public Permission settingPermission(DocPermission docPermission,
                                        DocPermissionGroup permissionGroup,
                                        Action action,
                                        Boolean canEdit) {
        Permission userPermissions = new Permission();
        userPermissions.setComment(docPermission.getComment());
        userPermissions.setCopy(docPermission.getCopy());
        userPermissions.setEdit(docPermission.getEdit());
        userPermissions.setPrint(docPermission.getPrint());
        userPermissions.setFillForms(docPermission.getFillForms());
        userPermissions.setModifyFilter(docPermission.getModifyFilter());
        userPermissions.setModifyContentControl(docPermission.getModifyFilterContent());
        userPermissions.setReview(docPermission.getReview());
        userPermissions.setChat(docPermission.getChat());

        userPermissions.setComment(
                !action.equals(Action.view)
                        && !action.equals(Action.fillForms)
                        && !action.equals(Action.embedded)
                        && !action.equals(Action.blockcontent)
        );

        userPermissions.setFillForms(
                !action.equals(Action.view)
                        && !action.equals(Action.comment)
                        && !action.equals(Action.embedded)
                        && !action.equals(Action.blockcontent)
        );

        userPermissions.setReview(canEdit
                && (action.equals(Action.review) || action.equals(Action.edit)));

        userPermissions.setEdit(canEdit
                && (action.equals(Action.view)
                || action.equals(Action.edit)
                || action.equals(Action.filter)
                || action.equals(Action.blockcontent)));

        CommentGroup commentGroup = new CommentGroup();
        String groupCode = permissionGroup.getGroupCode();
        List<String> group = Lists.newArrayList(groupCode);

        commentGroup.setView(group);
        commentGroup.setEdit(group);
        commentGroup.setRemove(group);
        userPermissions.setCommentGroups(commentGroup);

        userPermissions.setUserInfoGroups(group);

        return userPermissions;
    }
}
