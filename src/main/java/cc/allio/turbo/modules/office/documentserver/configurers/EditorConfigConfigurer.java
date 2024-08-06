package cc.allio.turbo.modules.office.documentserver.configurers;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.EditorConfig;

public interface EditorConfigConfigurer<W> extends Configurer<EditorConfig, W> {
    void configure(EditorConfig editorConfig, W wrapper);
}
