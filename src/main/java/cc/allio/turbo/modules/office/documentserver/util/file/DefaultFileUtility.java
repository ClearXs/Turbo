package cc.allio.turbo.modules.office.documentserver.util.file;

import cc.allio.turbo.modules.office.documentserver.models.enums.DocumentType;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Qualifier("default")
public class DefaultFileUtility implements FileUtility, InitializingBean {

    @Value("${turbo.office.document.docservice.viewed-docs}")
    private String docserviceViewedDocs;

    @Value("${turbo.office.document.docservice.edited-docs}")
    private String docserviceEditedDocs;

    @Value("${turbo.office.document.docservice.convert-docs}")
    private String docserviceConvertDocs;

    @Value("${turbo.office.document.docservice.fillforms-docs}")
    private String docserviceFillDocs;

    @Value("${turbo.office.document.storage.path}")
    private String storagePath;

    // document extensions
    private final List<String> extsDocument = Arrays.asList(
            ".doc", ".docx", ".docm",
            ".dot", ".dotx", ".dotm",
            ".odt", ".fodt", ".ott", ".rtf", ".txt",
            ".html", ".htm", ".mht", ".xml",
            ".pdf", ".djvu", ".fb2", ".epub", ".xps", ".oform");

    // spreadsheet extensions
    private final List<String> extsSpreadsheet = Arrays.asList(
            ".xls", ".xlsx", ".xlsm", ".xlsb",
            ".xlt", ".xltx", ".xltm",
            ".ods", ".fods", ".ots", ".csv");

    // presentation extensions
    private final List<String> extsPresentation = Arrays.asList(
            ".pps", ".ppsx", ".ppsm",
            ".ppt", ".pptx", ".pptm",
            ".pot", ".potx", ".potm",
            ".odp", ".fodp", ".otp");

    @Override
    public String getFilePath(Long docId, String filename) {
        return storagePath + StringPool.SLASH + docId + StringPool.SLASH + filename;
    }

    // get the document type
    public DocumentType getDocumentType(final String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();  // get file extension from its name
        // word type for document extensions
        if (extsDocument.contains(ext)) {
            return DocumentType.word;
        }

        // cell type for spreadsheet extensions
        if (extsSpreadsheet.contains(ext)) {
            return DocumentType.cell;
        }

        // slide type for presentation extensions
        if (extsPresentation.contains(ext)) {
            return DocumentType.slide;
        }

        // default file type is word
        return DocumentType.word;
    }

    // get file name from its URL
    public String getFileName(final String url) {
        if (url == null) {
            return "";
        }

        // get file name from the last part of URL
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        fileName = fileName.split("\\?")[0];
        return fileName;
    }

    // get file name without extension
    public String getFileNameWithoutExtension(final String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    // get file extension from URL
    public String getFileExtension(final String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        return fileExt.toLowerCase();
    }

    // get an editor internal extension
    public String getInternalExtension(final DocumentType type) {
        // .docx for word file type
        if (type.equals(DocumentType.word)) {
            return ".docx";
        }

        // .xlsx for cell file type
        if (type.equals(DocumentType.cell)) {
            return ".xlsx";
        }

        // .pptx for slide file type
        if (type.equals(DocumentType.slide)) {
            return ".pptx";
        }

        // the default file type is .docx
        return ".docx";
    }

    public List<String> getFillExts() {
        return Arrays.asList(docserviceFillDocs.split("\\|"));
    }

    // get file extensions that can be viewed
    public List<String> getViewedExts() {
        return Arrays.asList(docserviceViewedDocs.split("\\|"));
    }

    // get file extensions that can be edited
    public List<String> getEditedExts() {
        return Arrays.asList(docserviceEditedDocs.split("\\|"));
    }

    // get file extensions that can be converted
    public List<String> getConvertExts() {
        return Arrays.asList(docserviceConvertDocs.split("\\|"));
    }

    // get all the supported file extensions
    public List<String> getFileExts() {
        List<String> res = new ArrayList<>();

        res.addAll(getViewedExts());
        res.addAll(getEditedExts());
        res.addAll(getConvertExts());
        res.addAll(getFillExts());

        return res;
    }

    // generate the file path from file directory and name
    public Path generateFilepath(final String directory, final String fullFileName) {
        String fileName = getFileNameWithoutExtension(fullFileName);  // get file name without extension
        String fileExtension = getFileExtension(fullFileName);  // get file extension
        Path path = Paths.get(directory + fullFileName);  // get the path to the files with the specified name

        for (int i = 1; Files.exists(path); i++) {  // run through all the files with the specified name
            // get a name of each file without extension and add an index to it
            fileName = getFileNameWithoutExtension(fullFileName) + "(" + i + ")";

            // create a new path for this file with the correct name and extension
            path = Paths.get(directory + fileName + fileExtension);
        }

        path = Paths.get(directory + fileName + fileExtension);
        return path;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DocumentDescriptor.fileUtility = this;
    }
}