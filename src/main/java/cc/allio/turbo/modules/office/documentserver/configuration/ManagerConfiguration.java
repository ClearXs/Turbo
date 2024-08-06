package cc.allio.turbo.modules.office.documentserver.configuration;

import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import cc.allio.turbo.modules.office.documentserver.command.CommandManager;
import cc.allio.turbo.modules.office.documentserver.managers.document.DefaultDocumentManager;
import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.managers.history.DefaultHistoryManager;
import cc.allio.turbo.modules.office.documentserver.managers.history.HistoryManager;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.DefaultJwtManager;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.managers.template.SampleTemplateManager;
import cc.allio.turbo.modules.office.documentserver.managers.template.TemplateManager;
import cc.allio.turbo.modules.office.documentserver.storage.FileStorageMutator;
import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ManagerConfiguration {

    @Bean
    @Primary
    public DocumentManager defaultDocumentManager(FileStorageMutator storageMutator,
                                                  FileStoragePathBuilder storagePathBuilder,
                                                  FileUtility fileUtility,
                                                  ServiceConverter serviceConverter) {
        return new DefaultDocumentManager(storageMutator, storagePathBuilder, fileUtility, serviceConverter);
    }

    @Bean
    @Primary
    public HistoryManager defaultHistoryManager(FileStoragePathBuilder storagePathBuilder,
                                                DocumentManager documentManager,
                                                JwtManager jwtManager,
                                                FileUtility fileUtility) {
        return new DefaultHistoryManager(storagePathBuilder, documentManager, jwtManager, fileUtility);
    }

    @Bean
    @Primary
    public JwtManager defaultJwtManager() {
        return new DefaultJwtManager();
    }

    @Bean("sample")
    @Primary
    public TemplateManager sampleTemplateManger(DocumentManager documentManager,
                                                FileStoragePathBuilder storagePathBuilder,
                                                FileUtility fileUtility) {
        return new SampleTemplateManager(documentManager, storagePathBuilder, fileUtility);
    }

    @Bean
    @Primary
    public CommandManager commandManager(JwtManager jwtManager, ServiceConverter serviceConverter, DocumentProperties documentProperties) {
        return new CommandManager(jwtManager, serviceConverter, documentProperties);
    }
}
