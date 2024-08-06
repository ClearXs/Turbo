package cc.allio.turbo.modules.office.documentserver.managers.document;

import java.util.ArrayList;
import java.util.Map;

// specify the document manager functions
public interface DocumentManager {

    // get a file name with an index if the file with such a name already exists
    String getCorrectName(String fileName);

    String getHistoryFileUrl(String fileName, Integer version, String file, Boolean forDocumentServer);  // get file URL

    String getCallback(Long docId, Long fileId, String filename, String filepath);  // get the callback URL

    /**
     * get URL to download a file
     */
    String getDownloadUrl(String filepath);

    ArrayList<Map<String, Object>> getFilesInfo();  // get file information

    ArrayList<Map<String, Object>> getFilesInfo(String fileId);  // get file information by its ID

    //  get the path to the file version by the history path and file version
    String versionDir(String path, Integer version, boolean historyPath);

    // create demo document
    String createDemo(String fileExt, Boolean sample, String uid, String uname) throws Exception;

    String getCreateUrl(String fileName, Boolean sample);  // get URL to the created file
}
