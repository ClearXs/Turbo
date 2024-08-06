package cc.allio.turbo.modules.office.documentserver.models.configurations;

import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Goback {  // the settings for the Open file location menu button and upper right corner button

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Value("${turbo.office.document.custom.index}")
    private String indexMapping;

    @Getter
    private String url;  /* the absolute URL to the website address which will be opened
    when clicking the Open file location menu button */

    @PostConstruct
    private void init() {
        this.url = storagePathBuilder.getServerUrl(false) + indexMapping;
    }
}
