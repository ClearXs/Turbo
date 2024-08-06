package cc.allio.turbo.modules.office.documentserver.configurers;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.FileModel;

public interface FileConfigurer<W> extends Configurer<FileModel, W> {
    void configure(FileModel model, W wrapper);

    FileModel getFileModel( W wrapper);
}
