package cc.allio.turbo.modules.office.documentserver.configurers.implementations;

import cc.allio.turbo.modules.office.documentserver.configurers.DocumentConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.EditorConfigConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.FileConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.PermissionMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultDocumentWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.documentserver.models.enums.DocumentType;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.FileModel;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class DefaultFileConfigurer implements FileConfigurer<DefaultFileWrapper> {

    private final ObjectFactory<FileModel> fileModelObjectFactory;
    private final FileUtility fileUtility;
    private final JwtManager jwtManager;
    private final DocumentConfigurer<DefaultDocumentWrapper> documentConfigurer;
    private final EditorConfigConfigurer<DefaultFileWrapper> editorConfigConfigurer;
    private final PermissionMapper permissionMapper;

    public void configure(final FileModel fileModel, final DefaultFileWrapper wrapper) {  // define the file configurer
        if (fileModel != null) {  // check if the file model is specified
            String filename = wrapper.getFilename();  // get the fileName parameter from the file wrapper
            Action action = wrapper.getAction();  // get the action parameter from the file wrapper

            DocumentType documentType = fileUtility.getDocumentType(filename);  // get the document type of the specified file
            fileModel.setDocumentType(documentType);  // set the document type to the file model
            fileModel.setType(wrapper.getType());  // set the platform type to the file model

            String fileExt = fileUtility.getFileExtension(filename);
            boolean canEdit = fileUtility.getEditedExts().contains(fileExt);
            if ((!canEdit && action.equals(Action.edit) || action.equals(Action.fillForms))
                    && fileUtility.getFillExts().contains(fileExt)) {
                canEdit = true;
                wrapper.setAction(Action.fillForms);
            }
            wrapper.setCanEdit(canEdit);

            // define the document wrapper
            DefaultDocumentWrapper documentWrapper =
                    DefaultDocumentWrapper.builder()
                            .doc(wrapper.getDoc())
                            .fileId(wrapper.getFileId())
                            .filename(filename)
                            .filepath(wrapper.getFilepath())
                            .permission(permissionMapper.toModel(wrapper))
                            .favorite(true)
                            .isEnableDirectUrl(wrapper.getIsEnableDirectUrl())
                            .build();

            // define the document configurer
            documentConfigurer.configure(fileModel.getDocument(), documentWrapper);

            // define the editorConfig configurer
            editorConfigConfigurer.configure(fileModel.getEditorConfig(), wrapper);

            Map<String, Object> map = new HashMap<>();
            map.put("type", fileModel.getType());
            map.put("documentType", documentType);
            map.put("document", fileModel.getDocument());
            map.put("editorConfig", fileModel.getEditorConfig());

            fileModel.setToken(jwtManager.createToken(map));  // create a token and set it to the file model
        }
    }

    @Override
    public FileModel getFileModel(DefaultFileWrapper wrapper) {  // get file model
        FileModel fileModel = fileModelObjectFactory.getObject();
        configure(fileModel, wrapper);  // and configure it
        return fileModel;
    }
}
