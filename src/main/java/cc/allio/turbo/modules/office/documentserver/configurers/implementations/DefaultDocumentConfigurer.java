package cc.allio.turbo.modules.office.documentserver.configurers.implementations;

import cc.allio.turbo.modules.office.documentserver.configurers.DocumentConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.InfoMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultDocumentWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.models.configurations.Info;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.Document;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.Permission;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import cc.allio.uno.core.util.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultDocumentConfigurer implements DocumentConfigurer<DefaultDocumentWrapper> {

    private final DocumentManager documentManager;
    private final FileUtility fileUtility;
    private final ServiceConverter serviceConverter;
    private final InfoMapper infoMapper;

    @Override
    public void configure(final Document document, final DefaultDocumentWrapper wrapper) {
        // get the fileName parameter from the document wrapper
        String filepath = wrapper.getFilepath();
        String filename = wrapper.getFilename();
        // get the permission parameter from the document wrapper
        Permission permission = wrapper.getPermission();
        // set the title to the document config
        document.setTitle(filename);

        // set the URL to download a file to the document config
        document.setUrl(documentManager.getDownloadUrl(filepath));
        // set the file URL to the document config
        document.setUrlUser(documentManager.getDownloadUrl(filepath));
        document.setDirectUrl(
                Boolean.TRUE.equals(wrapper.getIsEnableDirectUrl()) ?
                        documentManager.getDownloadUrl(filepath) : "");
        // set the file type to the document config
        document.setFileType(fileUtility.getFileExtension(filename).replace(".", ""));

        // set info
        Info info = infoMapper.toModel(wrapper);
        document.setInfo(info);

        // get the document key
        String docKey = wrapper.getDoc().getDocKey();
        if (StringUtils.isBlank(docKey)) {
            // create new doc key and version
            docKey = serviceConverter.generateRevisionId(filename);
        }

        document.setKey(docKey);  // set the key to the document config
        document.setPermissions(permission);  // set the permission parameters to the document config
    }
}