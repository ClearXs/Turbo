package cc.allio.turbo.modules.office.documentserver.util;

import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.entity.DocHistory;
import cc.allio.turbo.modules.system.entity.SysAttachment;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * enhance {@link Doc} instance
 */
@Slf4j
public class DocumentDescriptor {

    @Getter
    private final Long docId;
    @Getter
    private final Integer version;
    @Getter
    private final String docKey;
    @Getter
    private Long creator;
    @Getter
    private final String createTime;
    private final String file;

    @Getter
    private final String title;
    @Getter
    private final String type;

    public static FileUtility fileUtility;

    public DocumentDescriptor(Doc doc) {
        this.docId = doc.getId();
        this.version = doc.getDocVersion();
        this.docKey = doc.getKey();
        this.creator = doc.getCreator();
        this.createTime = Optional.ofNullable(doc.getCreatedTime()).map(DateUtil::formatDate).orElse(null);
        this.file = doc.getFile();
        this.title = truncateTitle(doc.getTitle());
        this.type = truncateType(doc.getType());
    }

    public DocumentDescriptor(DocHistory doc) {
        this.docId = doc.getId();
        this.version = doc.getDocVersion();
        this.docKey = doc.getDocKey();
        this.createTime = Optional.ofNullable(doc.getCreatedTime()).map(DateUtil::formatDate).orElse(null);
        this.file = doc.getFile();
        this.title = truncateTitle(doc.getDocTitle());
        this.type = truncateType(doc.getDocType());
    }

    /**
     * get document saves filepath prefix
     *
     * @return like as /xx/xxx/
     */
    public String getDocFilePathPrefix() {
        SysAttachment documentAttachment = obtainAttachment();
        if (documentAttachment != null) {
            String filePath = documentAttachment.getFilepath();
            return filePath.substring(0, filePath.lastIndexOf(StringPool.SLASH));
        }
        return StringPool.EMPTY;
    }

    /**
     * from {@link Doc#getFile()} parse to document {@link SysAttachment} instance
     *
     * @return the {@link SysAttachment} instance or null
     */
    public SysAttachment obtainAttachment() {
        if (StringUtils.isBlank(file)) {
            return null;
        }
        List<SysAttachment> sysAttMainEntities = JsonUtils.readList(file, SysAttachment.class);
        if (CollectionUtils.isNotEmpty(sysAttMainEntities)) {
            return sysAttMainEntities.get(0);
        }
        return null;
    }

    /**
     * from {@link Doc#getFile()} parse to change data {@link SysAttachment} instance
     *
     * @return the {@link SysAttachment} instance or null
     */
    public SysAttachment obtainChangeData() {
        if (StringUtils.isBlank(file)) {
            return null;
        }
        List<SysAttachment> sysAttMainEntities = JsonUtils.readList(file, SysAttachment.class);
        if (CollectionUtils.isNotEmpty(sysAttMainEntities) && sysAttMainEntities.size() > 1) {
            return sysAttMainEntities.get(1);
        }
        return null;
    }

    /**
     * get document file fullname. contains file name and document file type
     *
     * @return
     */
    public String getFullname() {
        return getTitle() + StringPool.ORIGIN_DOT + getType();
    }

    /**
     * parse document title whether contains '.'
     *
     * @param title the document title
     * @return truncate title result
     */
    private String truncateTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return StringPool.EMPTY;
        }
        if (title.contains(StringPool.ORIGIN_DOT)) {
            return title.substring(0, title.lastIndexOf(StringPool.ORIGIN_DOT));
        }
        return title;
    }

    /**
     * parse document type whether contains '.'
     *
     * @param type the document type
     * @return truncate type result
     */
    public String truncateType(String type) {
        if (StringUtils.isBlank(type)) {
            return StringPool.EMPTY;
        }
        if (type.contains(StringPool.ORIGIN_DOT)) {
            return type.substring(type.indexOf(StringPool.ORIGIN_DOT) + 1);
        }
        return type;
    }

    /**
     * document key (like as [docId]sequence = 1111'0001), trim to 1
     *
     * @param docKey the document key
     * @return
     */
    public static Integer trimToVersion(String docKey) {
        try {
            String version = docKey.substring(docKey.length() - 4);
            return Integer.parseInt(version);
        } catch (NumberFormatException ex) {
            log.error("Failed to trim document key {}", docKey);
            return null;
        }
    }

    /**
     * from the {@link Doc} attachment get file type
     *
     * @param doc the {@link Doc} instance
     * @return empty or file type
     */
    public static String obtainTypeFromFile(Doc doc) {
        DocumentDescriptor documentDescriptor = new DocumentDescriptor(doc);
        SysAttachment documentAttachment = documentDescriptor.obtainAttachment();
        return Optional.ofNullable(documentAttachment)
                .flatMap(a ->
                        Optional.ofNullable(fileUtility)
                                .map(utility -> utility.getFileExtension(a.getFilename())))
                .orElse(StringPool.EMPTY);
    }

    /**
     * get document file fullname. contains file name and document file type
     *
     * @param doc the {@link Doc}
     * @return
     */
    public static String getFullname(Doc doc) {
        return new DocumentDescriptor(doc).getFullname();
    }
}
