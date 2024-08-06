package cc.allio.turbo.modules.office.documentserver.models.filemodel;

import cc.allio.turbo.modules.office.documentserver.models.configurations.Info;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class Document {  // the parameters pertaining to the document (title, url, file type, etc.)
    @Autowired
    private Info info;  /* additional parameters for the document (document owner, folder where the document is stored,
     uploading date, sharing settings) */
    @Autowired
    private Permission permissions;  // the permission for the document to be edited and downloaded or not
    private String fileType;  //  the file type for the source viewed or edited document
    private String key;  // the unique document identifier used by the service to recognize the document
    private String urlUser;  /* the absolute URL that will allow the document to be saved
    onto the user personal computer */
    private String title;  /* the desired file name for the viewed or edited document which will also be used
    as file name when the document is downloaded */
    private String url;  // the absolute URL where the source viewed or edited document is stored
    private String directUrl;
}
