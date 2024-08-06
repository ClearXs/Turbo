package cc.allio.turbo.modules.office.documentserver.configurers.implementations;

import cc.allio.turbo.modules.office.documentserver.configurers.CustomizationConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.EditorConfigConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.EmbeddedConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.mapper.UserMapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultCustomizationWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultEmbeddedWrapper;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.managers.template.TemplateManager;
import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.documentserver.models.enums.Mode;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.EditorConfig;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.User;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashMap;

@AllArgsConstructor
public class DefaultEditorConfigConfigurer implements EditorConfigConfigurer<DefaultFileWrapper> {

    private final DocumentManager documentManager;
    private final TemplateManager templateManager;
    private final CustomizationConfigurer<DefaultCustomizationWrapper> customizationConfigurer;
    private final EmbeddedConfigurer<DefaultEmbeddedWrapper> embeddedConfigurer;
    private final UserMapper userMapper;

    @SneakyThrows
    @Override
    public void configure(final EditorConfig config, final DefaultFileWrapper wrapper) {  // define the editorConfig configurer
        if (wrapper.getActionData() != null) {  // check if the actionData is not empty in the editorConfig wrapper

            // set actionLink to the editorConfig
            config.setActionLink(
                    JsonUtils.getJsonMapper().readValue(wrapper.getActionData(),
                            (JavaType) new TypeToken<HashMap<String, Object>>() {
                            }.getType()));
        }
        String filename = wrapper.getFilename();  // set the fileName parameter from the editorConfig wrapper
        String filepath = wrapper.getFilepath();

        boolean userIsAnon = false;

        // set a template to the editorConfig if the user is not anonymous
        config.setTemplates(templateManager.createTemplates(filename));
        config.setCallbackUrl(documentManager.getCallback(wrapper.getDoc().getDocId(), wrapper.getFileId(), filename, filepath));  // set the callback URL to the editorConfig

        // set the document URL where it will be created to the editorConfig if the user is not anonymous
        config.setCreateUrl(documentManager.getCreateUrl(filename, false));
        config.setLang(wrapper.getLang());  // set the language to the editorConfig
        Boolean canEdit = wrapper.getCanEdit();  // check if the file of the specified type can be edited or not
        Action action = wrapper.getAction();  // get the action parameter from the editorConfig wrapper
        config.setCoEditing(action.equals(Action.view) && userIsAnon ? new HashMap<>() {{
            put("mode", "strict");
            put("change", false);
        }} : null);

        customizationConfigurer.configure(config.getCustomization(),
                DefaultCustomizationWrapper.builder()  // define the customization configurer
                        .action(action)
                        .user(wrapper.getUser())
                        .build());
        config.setMode(Boolean.TRUE.equals(canEdit)
                && !action.equals(Action.view) ? Mode.edit : Mode.view);

        // set user
        User user = userMapper.toModel(wrapper);
        config.setUser(user);

        DefaultEmbeddedWrapper embeddedWrapper =
                DefaultEmbeddedWrapper.builder()
                        .docId(wrapper.getDoc().getDocId())
                        .type(wrapper.getType())
                        .filename(filename)
                        .filepath(filepath)
                        .build();
        embeddedConfigurer.configure(config.getEmbedded(), embeddedWrapper);
    }
}
