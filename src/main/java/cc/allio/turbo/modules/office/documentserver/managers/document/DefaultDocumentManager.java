package cc.allio.turbo.modules.office.documentserver.managers.document;

import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.office.documentserver.storage.FileStorageMutator;
import cc.allio.turbo.modules.office.documentserver.storage.FileStoragePathBuilder;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.turbo.modules.office.documentserver.util.service.ServiceConverter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static cc.allio.turbo.modules.office.documentserver.util.Constants.KILOBYTE_SIZE;

public class DefaultDocumentManager implements DocumentManager {

    @Value("${turbo.office.document.custom.track}")
    private String trackUrl;
    @Value("${turbo.office.document.custom.download}")
    private String downloadUrl;
    @Value("${turbo.office.document.custom.create}")
    private String createUrl;

    private final FileStorageMutator storageMutator;
    private final FileStoragePathBuilder storagePathBuilder;
    private final FileUtility fileUtility;
    private final ServiceConverter serviceConverter;

    public DefaultDocumentManager(FileStorageMutator storageMutator,
                                  FileStoragePathBuilder storagePathBuilder,
                                  FileUtility fileUtility,
                                  ServiceConverter serviceConverter) {
        this.storageMutator = storageMutator;
        this.storagePathBuilder = storagePathBuilder;
        this.fileUtility = fileUtility;
        this.serviceConverter = serviceConverter;
    }

    // get URL to the created file
    public String getCreateUrl(final String fileName, final Boolean sample) {
        String fileExt = fileUtility.getFileExtension(fileName).replace(".", "");
        String serverUrl = storagePathBuilder.getServerUrl(true);
        return serverUrl + createUrl + "?fileExt=" + fileExt + "&sample=" + sample;
    }

    // get a file name with an index if the file with such a name already exists
    public String getCorrectName(final String fileName) {
        String baseName = fileUtility.getFileNameWithoutExtension(fileName);  // get file name without extension
        String ext = fileUtility.getFileExtension(fileName);  // get file extension
        String name = baseName + ext;  // create a full file name

        Path path = Paths.get(storagePathBuilder.getFileLocation(name));

        // run through all the files with such a name in the storage directory
        for (int i = 1; Files.exists(path); i++) {
            name = baseName + " (" + i + ")" + ext;  // and add an index to the base name
            path = Paths.get(storagePathBuilder.getFileLocation(name));
        }

        return name;
    }

    // get file URL
    public String getHistoryFileUrl(String fileName,
                                    Integer version,
                                    String file,
                                    Boolean forDocumentServer) {
        try {
            String serverPath = storagePathBuilder.getServerUrl(forDocumentServer);  // get server URL
            String hostAddress = storagePathBuilder.getStorageLocation();  // get the storage directory
            String filePathDownload = !fileName.contains(InetAddress.getLocalHost().getHostAddress()) ? fileName
                    : fileName.substring(fileName.indexOf(InetAddress.getLocalHost().getHostAddress())
                    + InetAddress.getLocalHost().getHostAddress().length() + 1);
            String userAddress = Boolean.TRUE.equals(forDocumentServer) ? "&userAddress" + URLEncoder
                    .encode(hostAddress, StandardCharsets.UTF_8) : "";
            return serverPath + "/downloadhistory?fileName=" + URLEncoder
                    .encode(filePathDownload, StandardCharsets.UTF_8)
                    + "&ver=" + version + "&file=" + file
                    + userAddress;
        } catch (UnknownHostException e) {
            return "";
        }
    }

    // get the callback URL
    public String getCallback(Long docId, Long fileId, final String filename, String filepath) {
        String serverPath = storagePathBuilder.getServerUrl(true);
        String query =
                String.format(trackUrl + "/" + docId + "?filename=%s&filepath=%s&fileId=%s" + "&" + WebUtil.X_AUTHENTICATION + "=%s",
                        URLEncoder.encode(filename, StandardCharsets.UTF_8),
                        URLEncoder.encode(filepath, StandardCharsets.UTF_8),
                        fileId,
                        WebUtil.getToken());
        return serverPath + query;
    }

    // get URL to download a file
    @Override
    public String getDownloadUrl(String filepath) {
        // download?path=xxx
        return downloadUrl + "?path=" + filepath;
    }

    // get file information
    public ArrayList<Map<String, Object>> getFilesInfo() {
        ArrayList<Map<String, Object>> files = new ArrayList<>();

        // run through all the stored files
        for (File file : storageMutator.getStoredFiles()) {
            Map<String, Object> map = new LinkedHashMap<>();  // write all the parameters to the map
            map.put("version", storagePathBuilder.getFileVersion(file.getName(), false));
            map.put("id", serviceConverter
                    .generateRevisionId(storagePathBuilder.getStorageLocation()
                            + "/" + file.getName() + "/"
                            + Paths.get(storagePathBuilder.getFileLocation(file.getName()))
                            .toFile()
                            .lastModified()));
            map.put("contentLength", new BigDecimal(String.valueOf((file.length() / Double.valueOf(KILOBYTE_SIZE))))
                    .setScale(2, RoundingMode.HALF_UP) + " KB");
            map.put("pureContentLength", file.length());
            map.put("title", file.getName());
            map.put("updated", String.valueOf(new Date(file.lastModified())));
            files.add(map);
        }

        return files;
    }

    // get file information by its ID
    public ArrayList<Map<String, Object>> getFilesInfo(final String fileId) {
        ArrayList<Map<String, Object>> file = new ArrayList<>();
        for (Map<String, Object> map : getFilesInfo()) {
            if (map.get("id").equals(fileId)) {
                file.add(map);
                break;
            }
        }
        return file;
    }

    // get the path to the file version by the history path and file version
    public String versionDir(final String path, final Integer version, final boolean historyPath) {
        if (!historyPath) {
            return storagePathBuilder.getHistoryDir(storagePathBuilder.getFileLocation(path)) + version;
        }
        return path + File.separator + version;
    }

    // create demo document
    public String createDemo(final String fileExt, final Boolean sample, final String uid, final String uname) {
        String demoName = (Boolean.TRUE.equals(sample) ? "sample." : "new.")
                + fileExt;  // create sample or new template file with the necessary extension
        String demoPath = "assets" + File.separator + (Boolean.TRUE.equals(sample) ? "sample" : "new")
                + File.separator + demoName;  // get the path to the sample document

        // get a file name with an index if the file with such a name already exists
        String fileName = getCorrectName(demoName);

        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(demoPath);  // get the input file stream

        if (stream == null) {
            return null;
        }

        storageMutator.createFile(FileSystems.getDefault().getPath(storagePathBuilder.getFileLocation(fileName)), stream);  // create a file in the specified directory
        storageMutator.createMeta(fileName, uid, uname);  // create meta information of the demo file

        return fileName;
    }
}
