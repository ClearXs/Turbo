package cc.allio.turbo.modules.office.documentserver.configurers.mapper;

import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.User;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import cc.allio.turbo.modules.office.vo.DocUser;

/**
 * map to {@link User}
 *
 * @author j.x
 * @date 2024/5/9 22:57
 * @since 0.0.1
 */
public class UserMapper implements Mapper<DefaultFileWrapper, User> {

    @Override
    public User toModel(DefaultFileWrapper wrapper) {
        User user = new User();
        DocUser docUser = wrapper.getUser();
        DocPermissionGroup permissionGroup = docUser.getPermissionGroup();
        user.setId(docUser.getUserId().toString());
        user.setName(docUser.getUsername());
        user.setGroup(permissionGroup.getGroupCode());
        return user;
    }
}
