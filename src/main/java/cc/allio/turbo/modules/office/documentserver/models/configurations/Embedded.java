package cc.allio.turbo.modules.office.documentserver.models.configurations;

import cc.allio.turbo.modules.office.documentserver.models.enums.ToolbarDocked;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
/* The parameters which allow to change the settings
 which define the behavior of the buttons in the embedded mode */
public class Embedded {
    private String embedUrl;  /* the absolute URL to the document serving as a source file for the document embedded
     into the web page */
    private String saveUrl;  /* the absolute URL that will allow the document to be saved
     onto the user personal computer */
    private String shareUrl;  // the absolute URL that will allow other users to share this document
    private ToolbarDocked toolbarDocked;  // the place for the embedded viewer toolbar, can be either top or bottom
}
