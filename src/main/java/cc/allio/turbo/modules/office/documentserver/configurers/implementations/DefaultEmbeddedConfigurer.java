package cc.allio.turbo.modules.office.documentserver.configurers.implementations;

import cc.allio.turbo.modules.office.documentserver.configurers.EmbeddedConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultEmbeddedWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.models.configurations.Embedded;
import cc.allio.turbo.modules.office.documentserver.models.enums.ToolbarDocked;
import cc.allio.turbo.modules.office.documentserver.models.enums.Type;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultEmbeddedConfigurer implements EmbeddedConfigurer<DefaultEmbeddedWrapper> {

    private final DocumentManager documentManager;

    @Override
    public void configure(final Embedded embedded,
                          final DefaultEmbeddedWrapper wrapper) {  // define the embedded configurer
        if (wrapper.getType().equals(Type.embedded)) {  // check if the type from the embedded wrapper is embedded
            String url = documentManager.getDownloadUrl(wrapper.getFilepath());  // get file URL of the specified file

            /* set the embedURL parameter to the embedded config (the absolute URL to the document serving
             as a source file for the document embedded into the web page) */
            embedded.setEmbedUrl(url);

            /* set the saveURL parameter to the embedded config (the absolute URL that will allow
             the document to be saved onto the user personal computer) */
            embedded.setSaveUrl(url);

            /* set the shareURL parameter to the embedded config (the absolute URL
             that will allow other users to share this document) */
            embedded.setShareUrl(url);

            /* set the top toolbarDocked parameter to the embedded config (the place for the
             embedded viewer toolbar, can be either top or bottom) */
            embedded.setToolbarDocked(ToolbarDocked.top);
        }
    }
}
