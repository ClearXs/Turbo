package cc.allio.turbo.modules.office.documentserver.managers.template;

import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.models.enums.DocumentType;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.Template;
import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import com.google.common.collect.Lists;

import java.util.List;

public class SampleTemplateManager implements TemplateManager {

    private final DocumentManager documentManager;
    private final FileStoragePathBuilder storagePathBuilder;
    private final FileUtility fileUtility;

    public SampleTemplateManager(DocumentManager documentManager,
                                 FileStoragePathBuilder storagePathBuilder,
                                 FileUtility fileUtility) {
        this.documentManager = documentManager;
        this.storagePathBuilder = storagePathBuilder;
        this.fileUtility = fileUtility;
    }

    // create a template document with the specified name
    public List<Template> createTemplates(final String fileName) {
        List<Template> templates = Lists.newArrayList(
                new Template("", "Blank", documentManager
                        .getCreateUrl(fileName, false)),  // create a blank template
                new Template(getTemplateImageUrl(fileName), "With sample content", documentManager
                        .getCreateUrl(fileName,
                                true))  // create a template with sample content using the template image
        );
        return templates;
    }

    // get the template image URL for the specified file
    public String getTemplateImageUrl(final String fileName) {
        DocumentType fileType = fileUtility.getDocumentType(fileName);  // get the file type
        String path = storagePathBuilder.getServerUrl(true);  // get server URL
        if (fileType.equals(DocumentType.word)) {  // get URL to the template image for the word document type
            return path + "/css/img/file_docx.svg";
        } else if (fileType.equals(DocumentType.slide)) {  // get URL to the template image for the slide document type
            return path + "/css/img/file_pptx.svg";
        } else if (fileType.equals(DocumentType.cell)) {  // get URL to the template image for the cell document type
            return path + "/css/img/file_xlsx.svg";
        }
        return path + "/css/img/file_docx.svg";  // get URL to the template image for the default document type (word)
    }
}
