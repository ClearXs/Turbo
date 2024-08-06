package cc.allio.turbo.modules.office.documentserver.configuration;

import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import cc.allio.turbo.modules.office.documentserver.configurers.*;
import cc.allio.turbo.modules.office.documentserver.configurers.implementations.*;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.InfoMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.PermissionMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.UserMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultCustomizationWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultDocumentWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultEmbeddedWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.managers.template.TemplateManager;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.FileModel;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import cc.allio.turbo.modules.system.service.ISysUserService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigurerConfiguration {

    @Bean
    @Primary
    public CustomizationConfigurer<DefaultCustomizationWrapper> defaultCustomizationConfigurer(DocumentProperties documentProperties) {
        return new DefaultCustomizationConfigurer(documentProperties.getCustomization());
    }

    @Bean
    @Primary
    public DocumentConfigurer<DefaultDocumentWrapper> defaultDocumentConfigurer(DocumentManager documentManager,
                                                                                FileUtility fileUtility,
                                                                                ServiceConverter serviceConverter,
                                                                                InfoMapper infoMapper) {
        return new DefaultDocumentConfigurer(documentManager, fileUtility, serviceConverter, infoMapper);
    }

    @Bean
    public PermissionMapper permissionMapper(FileUtility fileUtility) {
        return new PermissionMapper(fileUtility);
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public InfoMapper infoMapper(ISysUserService sysUserService) {
        return new InfoMapper(sysUserService);
    }

    @Bean
    @Primary
    public EditorConfigConfigurer<DefaultFileWrapper> defaultEditorConfigConfigurer(DocumentManager documentManager,
                                                                                    TemplateManager templateManager,
                                                                                    CustomizationConfigurer<DefaultCustomizationWrapper> customizationConfigurer,
                                                                                    EmbeddedConfigurer<DefaultEmbeddedWrapper> embeddedConfigurer,
                                                                                    UserMapper userMapper) {
        return new DefaultEditorConfigConfigurer(documentManager, templateManager, customizationConfigurer, embeddedConfigurer, userMapper);
    }

    @Bean
    @Primary
    public EmbeddedConfigurer<DefaultEmbeddedWrapper> defaultEmbeddedConfigurer(DocumentManager documentManager) {
        return new DefaultEmbeddedConfigurer(documentManager);
    }

    @Bean
    @Primary
    public FileConfigurer<DefaultFileWrapper> defaultFileConfigurer(ObjectFactory<FileModel> fileModelObjectFactory,
                                                                    FileUtility fileUtility,
                                                                    JwtManager jwtManager,
                                                                    DocumentConfigurer<DefaultDocumentWrapper> documentConfigurer,
                                                                    EditorConfigConfigurer<DefaultFileWrapper> editorConfigConfigurer,
                                                                    PermissionMapper permissionMapper) {
        return new DefaultFileConfigurer(fileModelObjectFactory, fileUtility, jwtManager, documentConfigurer, editorConfigConfigurer, permissionMapper);
    }
}
