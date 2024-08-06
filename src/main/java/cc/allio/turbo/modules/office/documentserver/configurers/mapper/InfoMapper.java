package cc.allio.turbo.modules.office.documentserver.configurers.mapper;

import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultDocumentWrapper;
import cc.allio.turbo.modules.office.documentserver.models.configurations.Info;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.service.ISysUserService;

/**
 * map to {@link Info}
 *
 * @author j.x
 * @date 2024/5/13 14:11
 * @since 0.0.1
 */
public class InfoMapper implements Mapper<DefaultDocumentWrapper, Info> {

    private final ISysUserService sysUserService;

    public InfoMapper(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public Info toModel(DefaultDocumentWrapper wrapper) {
        DocumentDescriptor doc = wrapper.getDoc();
        Info info = new Info();
        Long creator = doc.getCreator();
        if (creator != null) {
            // set to username
            SysUser sysUser = sysUserService.getById(creator);
            info.setOwner(sysUser.getNickname());
        }

        info.setUploaded(doc.getCreateTime());
        return info;
    }
}
