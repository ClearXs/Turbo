package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.office.configuration.properties.Docservice;
import cc.allio.turbo.modules.office.configuration.properties.DocumentProperties;
import cc.allio.turbo.modules.office.configuration.properties.Mention;
import cc.allio.turbo.modules.office.documentserver.configurers.FileConfigurer;
import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.models.Editor;
import cc.allio.turbo.modules.office.documentserver.models.enums.Action;
import cc.allio.turbo.modules.office.documentserver.models.enums.Type;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.FileModel;
import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import cc.allio.turbo.modules.office.documentserver.vo.Mentions;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.service.IDocService;
import cc.allio.turbo.modules.office.service.IDocUserService;
import cc.allio.turbo.modules.office.vo.DocUser;
import cc.allio.turbo.modules.system.entity.SysAttachment;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/office/editor")
@Tag(name = "编辑器")
public class EditorController {

    private final FileStoragePathBuilder storagePathBuilder;
    private final JwtManager jwtManager;
    private final FileConfigurer<DefaultFileWrapper> fileConfigurer;
    private final IDocService docService;
    private final IDocUserService docUserService;
    private final DocumentProperties documentProperties;

    @GetMapping
    @Operation(summary = "获取编辑器配置")
    public R<Editor> getEditorConfig(@RequestParam("docId") final Long docId,
                                     @RequestParam(value = "action", required = false) final String actionParam,
                                     @RequestParam(value = "type", required = false) final String typeParam,
                                     @RequestParam(value = "actionLink", required = false) final String actionLink,
                                     @RequestParam(value = "directUrl", required = false, defaultValue = "false") final Boolean directUrl,
                                     HttpServletRequest request) {
        Locale locale = request.getLocale();
        Action action = Action.edit;
        Type type = Type.desktop;

        if (actionParam != null) {
            action = Action.valueOf(actionParam);
        }

        if (typeParam != null) {
            type = Type.valueOf(typeParam);
        }

        Doc doc = docService.getById(docId);
        if (doc == null) {
            return R.internalError("not found document!");
        }

        String file = doc.getFile();
        List<SysAttachment> attachments;
        try {
            attachments = JsonUtils.readList(file, SysAttachment.class);
        } catch (Throwable ex) {
            log.error("Failed to parse attachment", ex);
            return R.internalError("Failed to parse attachment!");
        }

        if (CollectionUtils.isEmpty(attachments)) {
            return R.internalError("not found document!");
        }
        SysAttachment attachment = attachments.get(0);

        // find user
        DocUser docUser;
        try {
            docUser = docUserService.getDocUserByRequest(docId);
        } catch (BizException ex) {
            return R.internalError(ex.getMessage());
        }
        // get file model with the default file parameters
        DocumentDescriptor documentDescriptor = new DocumentDescriptor(doc);
        DefaultFileWrapper fileWrapper = DefaultFileWrapper
                .builder()
                .doc(documentDescriptor)
                .fileId(attachment.getId())
                // fullname
                .filename(documentDescriptor.getFullname())
                .filepath(Optional.of(attachment).map(SysAttachment::getFilepath).orElse(null))
                .type(type)
                .lang(locale.toLanguageTag())
                .action(action)
                .user(docUser)
                .actionData(actionLink)
                .isEnableDirectUrl(directUrl)
                .build();
        FileModel fileModel = fileConfigurer.getFileModel(fileWrapper);

        Editor editor = new Editor();

        // add attributes to the specified model
        // add file model with the default parameters to the original model
        editor.setModel(fileModel);

        // create the document service api URL and add it to the model

        Docservice.Url url = documentProperties.getDocservice().getUrl();
        String onlyofficeServerUrl = documentProperties.getOnlyofficeServerUrl();
        editor.setDocserviceApiUrl(onlyofficeServerUrl + url.getApi());

        // get an image and add it to the model
        editor.setDataInsertImage(getInsertImage(directUrl));

        // get a document for comparison and add it to the model
        editor.setDataCompareFile(getCompareFile(directUrl));

        // get recipients data for mail merging and add it to the model
        editor.setDataMailMergeRecipients(getMailMerge(directUrl));

        // get user data for mentions and add it to the model
        editor.setUsersForMentions(getDefaultMentions());
        editor.setDocumentServerUrl(documentProperties.getOnlyofficeServerUrl());

        return R.ok(editor);
    }

    private List<Mentions> getDefaultMentions() {
        List<Mention> mentions = documentProperties.getMentions();
        String mentionsJson = JsonUtils.toJson(mentions);
        return JsonUtils.readList(mentionsJson, Mentions.class);
    }

    @SneakyThrows
    private String getInsertImage(Boolean directUrl) {  // get an image that will be inserted into the document
        Map<String, Object> dataInsertImage = new HashMap<>();
        dataInsertImage.put("fileType", "png");
        dataInsertImage.put("url", storagePathBuilder.getServerUrl(true) + "/css/img/logo.png");
        if (Boolean.TRUE.equals(directUrl)) {
            dataInsertImage.put("directUrl", storagePathBuilder
                    .getServerUrl(false) + "/css/img/logo.png");
        }

        // check if the document token is enabled
        if (jwtManager.tokenEnabled()) {

            // create token from the dataInsertImage object
            dataInsertImage.put("token", jwtManager.createToken(dataInsertImage));
        }

        return JsonUtils.toJson(dataInsertImage).substring(1, JsonUtils.toJson(dataInsertImage).length() - 1);
    }

    // get a document that will be compared with the current document
    @SneakyThrows
    private String getCompareFile(Boolean directUrl) {
        Map<String, Object> dataCompareFile = new HashMap<>();
        dataCompareFile.put("fileType", "docx");
        dataCompareFile.put("url", storagePathBuilder.getServerUrl(true) + "/assets?name=sample.docx");
        if (Boolean.TRUE.equals(directUrl)) {
            dataCompareFile.put("directUrl", storagePathBuilder
                    .getServerUrl(false) + "/assets?name=sample.docx");
        }

        // check if the document token is enabled
        if (jwtManager.tokenEnabled()) {

            // create token from the dataCompareFile object
            dataCompareFile.put("token", jwtManager.createToken(dataCompareFile));
        }
        return JsonUtils.toJson(dataCompareFile);
    }

    @SneakyThrows
    private String getMailMerge(Boolean directUrl) {
        Map<String, Object> dataMailMergeRecipients = new HashMap<>();  // get recipients data for mail merging
        dataMailMergeRecipients.put("fileType", "csv");
        dataMailMergeRecipients.put("url", storagePathBuilder.getServerUrl(true) + "/csv");
        if (Boolean.TRUE.equals(directUrl)) {
            dataMailMergeRecipients.put("directUrl", storagePathBuilder.getServerUrl(false) + "/csv");
        }

        // check if the document token is enabled
        if (jwtManager.tokenEnabled()) {

            // create token from the dataMailMergeRecipients object
            dataMailMergeRecipients.put("token", jwtManager.createToken(dataMailMergeRecipients));
        }
        return JsonUtils.toJson(dataMailMergeRecipients);
    }
}
