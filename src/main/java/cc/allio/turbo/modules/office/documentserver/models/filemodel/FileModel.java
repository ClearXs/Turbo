package cc.allio.turbo.modules.office.documentserver.models.filemodel;

import cc.allio.turbo.modules.office.documentserver.models.enums.DocumentType;
import cc.allio.turbo.modules.office.documentserver.models.enums.Type;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * the file base parameters which include the platform type used,
 * document display size (width and height) and type of the document opened
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class FileModel {
    @Autowired
    private Document document;  // the parameters pertaining to the document (title, url, file type, etc.)
    private DocumentType documentType;  // the document type to be opened
    @Autowired
    private EditorConfig editorConfig;  /*  the parameters pertaining to the
     editor interface: opening mode (viewer or editor), interface language, additional buttons, etc. */
    private String token;  // the encrypted signature added to the Document Server config
    private Type type;  // the platform type used to access the document
}
