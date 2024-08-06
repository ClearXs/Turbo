package cc.allio.turbo.modules.office.documentserver.configurers;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.Document;

public interface DocumentConfigurer<W> extends Configurer<Document, W> {
    void configure(Document document, W wrapper);
}
