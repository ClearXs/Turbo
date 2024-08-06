package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import cc.allio.turbo.modules.office.mapper.DocPermissionGroupMapper;
import cc.allio.turbo.modules.office.service.IDocPermissionGroupService;
import cc.allio.turbo.modules.office.vo.DocPermission;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class DocPermissionGroupServiceImpl extends TurboCrudServiceImpl<DocPermissionGroupMapper, DocPermissionGroup>
        implements IDocPermissionGroupService, InitializingBean {

    private DocPermissionGroup adminGroup;
    private DocPermissionGroup defaultGroup;

    private static final DocPermission admin;

    static {
        admin = new DocPermission();
        admin.setChat(true);
        admin.setEdit(true);
        admin.setComment(true);
        admin.setCopy(true);
        admin.setPrint(true);
        admin.setFillForms(true);
        admin.setReview(true);
        admin.setTemplates(true);
        admin.setModifyFilter(true);
        admin.setModifyFilterContent(true);
    }

    private static final DocPermission defaultPermission;

    static {
        defaultPermission = new DocPermission();
        defaultPermission.setChat(true);
        defaultPermission.setEdit(true);
        defaultPermission.setComment(true);
        defaultPermission.setPrint(true);
        defaultPermission.setReview(true);
        defaultPermission.setCopy(true);
        defaultPermission.setModifyFilter(true);
        defaultPermission.setModifyFilterContent(true);
        defaultPermission.setTemplates(false);
    }

    @Override
    public Boolean settingDocPermissionGroup(Long docId) {
        DocPermissionGroup docAdminGroup = new DocPermissionGroup();
        BeanUtils.copyProperties(adminGroup, docAdminGroup);
        docAdminGroup.setDocId(docId);
        DocPermissionGroup docDefaultGroup = new DocPermissionGroup();
        BeanUtils.copyProperties(defaultGroup, docDefaultGroup);
        docDefaultGroup.setDocId(docId);
        return save(docAdminGroup) && save(docDefaultGroup);
    }

    @Override
    public DocPermissionGroup selectAdminPermissionGroup(Long docId) {
        return getOne(
                Wrappers.<DocPermissionGroup>lambdaQuery()
                        .eq(DocPermissionGroup::getDocId, docId)
                        .eq(DocPermissionGroup::getGroupCode, adminGroup.getGroupCode()));
    }

    @Override
    public DocPermissionGroup selectDefaultPermissionGroup(Long docId) {
        return getOne(
                Wrappers.<DocPermissionGroup>lambdaQuery()
                        .eq(DocPermissionGroup::getDocId, docId)
                        .eq(DocPermissionGroup::getGroupCode, defaultGroup.getGroupCode()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        adminGroup = new DocPermissionGroup();
        adminGroup.setGroupName("admin");
        adminGroup.setGroupCode("admin");
        adminGroup.setPermission(JsonUtils.toJson(admin));

        defaultGroup = new DocPermissionGroup();
        defaultGroup.setGroupName("默认");
        defaultGroup.setGroupCode("default");
        defaultGroup.setPermission(JsonUtils.toJson(defaultPermission));
    }
}