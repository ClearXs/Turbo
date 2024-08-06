package cc.allio.turbo.modules.office.documentserver.util.file;

import cc.allio.turbo.modules.office.configuration.properties.Storage;
import cc.allio.turbo.modules.office.documentserver.models.enums.DocumentType;

import java.nio.file.Path;
import java.util.List;

// specify the file utility functions
public interface FileUtility {

    /**
     * from current userId and {@link Storage#path} build file path
     *
     * @return
     */
    String getFilePath(Long docId, String filename);

    /**
     * get the document type
     */
    DocumentType getDocumentType(String fileName);

    /**
     *  get file name from its URL
     */
    String getFileName(String url);

    /**
     * get file name without extension
     */
    String getFileNameWithoutExtension(String url);

    /**
     *  get file extension from URL
     */
    String getFileExtension(String url);

    /**
     * get an editor internal extension
     */
    String getInternalExtension(DocumentType type);

    /**
     * get all the supported file extensions
     */
    List<String> getFileExts();

    /**
     * get file extensions that can be filled
     */
    List<String> getFillExts();

    /**
     *  get file extensions that can be viewed
     */
    List<String> getViewedExts();

    /**
     * get file extensions that can be edited
     */
    List<String> getEditedExts();

    /**
     *  get file extensions that can be converted
     */
    List<String> getConvertExts();

    /**
     * generate the file path from file directory and name
     */
    Path generateFilepath(String directory, String fullFileName);
}
