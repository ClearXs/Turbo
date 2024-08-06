
package cc.allio.turbo.modules.office.documentserver.managers.history;

import cc.allio.turbo.modules.office.documentserver.managers.document.DocumentManager;
import cc.allio.turbo.modules.office.documentserver.managers.jwt.JwtManager;
import cc.allio.turbo.modules.office.documentserver.models.filemodel.Document;
import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class DefaultHistoryManager implements HistoryManager {

    private final FileStoragePathBuilder storagePathBuilder;
    private final DocumentManager documentManager;
    private final JwtManager jwtManager;
    private final FileUtility fileUtility;

    public DefaultHistoryManager(FileStoragePathBuilder storagePathBuilder,
                                 DocumentManager documentManager,
                                 JwtManager jwtManager,
                                 FileUtility fileUtility) {
        this.storagePathBuilder = storagePathBuilder;
        this.documentManager = documentManager;
        this.jwtManager = jwtManager;
        this.fileUtility = fileUtility;
    }

    @SneakyThrows
    public String[] getHistory(final Document document) {  // get document history

        // get history directory
        String histDir = storagePathBuilder.getHistoryDir(storagePathBuilder.getFileLocation(document.getTitle()));
        int curVer = storagePathBuilder.getFileVersion(histDir, false);  // get current file version

        if (curVer > 0) {  // check if the current file version is greater than 0
            List<Object> hist = new ArrayList<>();
            Map<String, Object> histData = new HashMap<>();

            for (int i = 1; i <= curVer; i++) {  // run through all the file versions
                Map<String, Object> obj = new HashMap<String, Object>();
                Map<String, Object> dataObj = new HashMap<String, Object>();
                String verDir = documentManager
                        .versionDir(histDir, i, true);  // get the path to the given file version

                String key = i == curVer ? document.getKey() : readFileToEnd(new File(verDir
                        + File.separator + "key.txt"));  // get document key
                obj.put("key", key);
                obj.put("version", i);

                if (i == 1) {  // check if the version number is equal to 1
                    String createdInfo = readFileToEnd(new File(histDir
                            + File.separator + "createdInfo.json"));  // get file with meta data

                    if (StringUtils.isBlank(createdInfo)) {
                        continue;
                    }

                    JsonNode json = JsonUtils.readTree(createdInfo);

                    // write meta information to the object (user information and creation date)
                    obj.put("created", json.get("created").asText());
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", json.get("id").asText());
                    user.put("name", json.get("name").asText());
                    obj.put("user", user);
                }

                dataObj.put("fileType", fileUtility
                        .getFileExtension(document.getTitle()).replace(".", ""));
                dataObj.put("key", key);
                dataObj.put("url", i == curVer ? document.getUrl()
                        : documentManager.getHistoryFileUrl(document.getTitle(), i, "prev" + fileUtility
                        .getFileExtension(document.getTitle()), true));
                if (!document.getDirectUrl().equals("")) {
                    dataObj.put("directUrl", i == curVer ? document.getDirectUrl()
                            : documentManager.getHistoryFileUrl(document.getTitle(), i, "prev" + fileUtility
                            .getFileExtension(document.getTitle()), false));
                }
                dataObj.put("version", i);

                if (i > 1) {  //check if the version number is greater than 1
                    // if so, get the path to the changes.json file

                    ArrayNode changes = (ArrayNode) JsonUtils.readTree(readFileToEnd(new File(documentManager
                            .versionDir(histDir, i - 1, true) + File.separator + "changes.json")));

                    JsonNode change = changes.get(0);


                    // write information about changes to the object
                    obj.put("changes", changes.get("changes"));
                    obj.put("serverVersion", changes.get("serverVersion"));
                    obj.put("created", change.get("created").asText());
                    obj.put("user", change.get("user").asText());

                    // get the history data from the previous file version
                    Map<String, Object> prev = (Map<String, Object>) histData.get(Integer.toString(i - 2));
                    Map<String, Object> prevInfo = new HashMap<String, Object>();
                    prevInfo.put("fileType", prev.get("fileType"));
                    prevInfo.put("key", prev.get("key"));  // write key and URL information about previous file version
                    prevInfo.put("url", prev.get("url"));
                    if (!document.getDirectUrl().equals("")) {
                        prevInfo.put("directUrl", prev.get("directUrl"));
                    }

                    // write information about previous file version to the data object
                    dataObj.put("previous", prevInfo);
                    // write the path to the diff.zip archive with differences in this file version
                    Integer verdiff = i - 1;
                    dataObj.put("changesUrl", documentManager
                            .getHistoryFileUrl(document.getTitle(), verdiff, "diff.zip", true));
                }

                if (jwtManager.tokenEnabled()) {
                    dataObj.put("token", jwtManager.createToken(dataObj));
                }

                hist.add(obj);
                histData.put(Integer.toString(i - 1), dataObj);
            }

            // write history information about the current file version to the history object
            Map<String, Object> histObj = new HashMap<>();
            histObj.put("currentVersion", curVer);
            histObj.put("history", hist);
            return new String[]{JsonUtils.toJson(histObj), JsonUtils.toJson(histData)};
        }
        return new String[]{"", ""};
    }

    // read a file
    private String readFileToEnd(final File file) {
        String output = "";
        try {
            try (FileInputStream is = new FileInputStream(file)) {
                Scanner scanner = new Scanner(is);  // read data from the source
                scanner.useDelimiter("\\A");
                while (scanner.hasNext()) {
                    output += scanner.next();
                }
                scanner.close();
            }
        } catch (Exception e) {
        }
        return output;
    }
}
