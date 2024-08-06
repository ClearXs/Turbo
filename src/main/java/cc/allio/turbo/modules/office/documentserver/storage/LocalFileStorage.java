package cc.allio.turbo.modules.office.documentserver.storage;

import cc.allio.turbo.common.util.InetUtil;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.office.documentserver.util.file.FileUtility;
import cc.allio.uno.core.util.IoUtils;
import cc.allio.uno.core.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cc.allio.turbo.modules.office.documentserver.util.Constants.FILE_SAVE_TIMEOUT;
import static cc.allio.turbo.modules.office.documentserver.util.Constants.KILOBYTE_SIZE;

@Component
@Primary
public class LocalFileStorage implements FileStorageMutator, FileStoragePathBuilder {

    @Getter
    private String storageAddress;

    @Value("${turbo.office.document.docservice.url.example}")
    private String docserviceUrlExample;

    @Value("${turbo.office.document.docservice.history.postfix}")
    private String historyPostfix;

    @Autowired
    private FileUtility fileUtility;

    /*
        This Storage configuration method should be called whenever a new storage folder is required
     */
    public void configure(final String address) {
        this.storageAddress = address;
        if (this.storageAddress == null) {
            try {
                this.storageAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                this.storageAddress = "unknown_storage";
            }
        }
        this.storageAddress.replaceAll("[^0-9a-zA-Z.=]", "_");
        createDirectory(Paths.get(getStorageLocation()));
    }

    // get the storage directory
    public String getStorageLocation() {
        String serverPath = System.getProperty("user.dir");  // get the path to the server
        return serverPath + "/doc";
    }

    // get the directory of the specified file
    public String getFileLocation(final String fileName) {
        if (fileName.contains(File.separator)) {
            return getStorageLocation() + fileName;
        }
        return getStorageLocation() + fileUtility.getFileName(fileName);
    }

    @Override
    @SneakyThrows
    public byte[] getDownloadFile(String downloadUrl) {
        if (downloadUrl == null || downloadUrl.isEmpty()) {
            throw new RuntimeException("Url argument is not specified");  // URL isn't specified
        }

        URL uri = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setConnectTimeout(FILE_SAVE_TIMEOUT);
        InputStream stream = connection.getInputStream();  // get input stream of the file information from the URL

        int statusCode = connection.getResponseCode();
        if (statusCode != HttpStatus.OK.value()) {  // checking status code
            connection.disconnect();
            throw new RuntimeException("Document editing service returned status: " + statusCode);
        }

        if (stream == null) {
            connection.disconnect();
            throw new RuntimeException("Input stream is null");
        }
        return IoUtils.readToByteArray(stream);
    }

    // create a new directory if it does not exist
    public void createDirectory(final Path path) {
        if (Files.exists(path)) {
            return;
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create a new file if it does not exist
    public boolean createFile(final Path path, final InputStream stream) {
        if (Files.exists(path)) {
            return true;
        }
        try {
            File file = Files.createFile(path).toFile();  // create a new file in the specified path
            try (FileOutputStream out = new FileOutputStream(file)) {
                int read;
                final byte[] bytes = new byte[KILOBYTE_SIZE];
                while ((read = stream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);  // write bytes to the output stream
                }
                out.flush();  // force write data to the output stream that can be cached in the current thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // delete a file
    public boolean deleteFile(final String fileNameParam) {
        // decode a x-www-form-urlencoded string
        String fileName = URLDecoder.decode(fileNameParam, StandardCharsets.UTF_8);
        ;

        if (StringUtils.isBlank(fileName)) {
            return false;
        }

        String filenameWithoutExt = fileUtility.getFileNameWithoutExtension(fileName);  // get file name without extension

        Path filePath = fileName.contains(File.separator)
                ? Paths.get(fileName) : Paths.get(getFileLocation(fileName));  // get the path to the file
        Path filePathWithoutExt = fileName.contains(File.separator)
                ? Paths.get(filenameWithoutExt) : Paths
                .get(getStorageLocation() + filenameWithoutExt);  // get the path to the file without extension

        // delete the specified file; for directories, recursively delete any nested directories or files as well
        boolean fileDeleted = FileSystemUtils.deleteRecursively(filePath.toFile());
        /* delete the specified file without extension; for directories,
         recursively delete any nested directories or files as well */
        boolean fileWithoutExtDeleted = FileSystemUtils.deleteRecursively(filePathWithoutExt.toFile());

        return fileDeleted && fileWithoutExtDeleted;
    }

    // delete file history
    public boolean deleteFileHistory(final String fileNameParam) {
        String fileName = null;  // decode a x-www-form-urlencoded string
        try {
            fileName = URLDecoder.decode(fileNameParam, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        if (StringUtils.isBlank(fileName)) {
            return false;
        }

        Path fileHistoryPath = Paths
                .get(getStorageLocation() + getHistoryDir(fileName));  // get the path to the history file
        Path fileHistoryPathWithoutExt = Paths.get(getStorageLocation() + getHistoryDir(fileUtility
                .getFileNameWithoutExtension(fileName)));  // get the path to the history file without extension

        /* delete the specified history file; for directories,
         recursively delete any nested directories or files as well */
        boolean historyDeleted = FileSystemUtils.deleteRecursively(fileHistoryPath.toFile());

        /* delete the specified history file without extension; for directories,
         recursively delete any nested directories or files as well */
        boolean historyWithoutExtDeleted = FileSystemUtils.deleteRecursively(fileHistoryPathWithoutExt.toFile());

        return historyDeleted || historyWithoutExtDeleted;
    }

    // update a file
    public String updateFile(final String fileName, final byte[] bytes) {
        Path path = fileUtility
                .generateFilepath(getStorageLocation(), fileName);  // generate the path to the specified file
        try {
            Files.write(path, bytes);  // write new information in the bytes format to the file
            return path.getFileName().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // move a file to the specified destination
    public boolean moveFile(final Path source, final Path destination) {
        try {
            Files.move(source, destination,
                    new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // write the payload to the file
    public boolean writeToFile(final String pathName, final String payload) {
        try (FileWriter fw = new FileWriter(pathName)) {
            fw.write(payload);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // get the path where all the forcely saved file versions are saved or create it
    public String getForcesavePath(final String fileName, final Boolean create) {
        String directory = getStorageLocation();

        Path path = Paths.get(directory);  // get the storage directory
        if (!Files.exists(path)) {
            return "";
        }

        directory = getFileLocation(fileName) + historyPostfix + File.separator;

        path = Paths.get(directory);   // get the history file directory
        if (!create && !Files.exists(path)) {
            return "";
        }

        createDirectory(path);  // create a new directory where all the forcely saved file versions will be saved

        directory = directory + fileName;
        path = Paths.get(directory);
        if (!create && !Files.exists(path)) {
            return "";
        }

        return directory;
    }

    // load file as a resource
    public Resource loadFileAsResource(final String fileName) {
        String fileLocation = getForcesavePath(fileName,
                false);  // get the path where all the forcely saved file versions are saved
        if (StringUtils.isBlank(fileLocation)) {  // if file location is empty
            fileLocation = getFileLocation(fileName);  // get it by the file name
        }
        try {
            Path filePath = Paths.get(fileLocation);  // get the path to the file location
            Resource resource = new UrlResource(filePath.toUri());  // convert the file path to URL
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Resource loadFileAsResourceHistory(final String fileName, final String version, final String file) {

        String fileLocation = getStorageLocation() + fileName + "-hist" + File.separator + version
                + File.separator + file;  // get it by the file name

        try {
            Path filePath = Paths.get(fileLocation);  // get the path to the file location
            Resource resource = new UrlResource(filePath.toUri());  // convert the file path to URL
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get a collection of all the stored files
    public File[] getStoredFiles() {
        File file = new File(getStorageLocation());
        return file.listFiles(pathname -> pathname.isFile());
    }

    @SneakyThrows
    public void createMeta(final String fileName,
                           final String uid,
                           final String uname) {  // create the file meta information
        String histDir = getHistoryDir(getFileLocation(fileName));  // get the history directory

        Path path = Paths.get(histDir);  // get the path to the history directory
        createDirectory(path);  // create the history directory

        // create the json object with the file metadata
        JSONObject json = new JSONObject();
        json.put("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));  // put the file creation date to the json object
        json.put("id", uid);  // put the user ID to the json object
        json.put("name", uname);  // put the user name to the json object

        File meta = new File(histDir + File.separator
                + "createdInfo.json");  // create the createdInfo.json file with the file meta information
        try (FileWriter writer = new FileWriter(meta)) {
            json.writeJSONString(writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // create or update a file
    public boolean createOrUpdateFile(final Path path, final ByteArrayInputStream stream) {
        // TODO 考虑如何保存文件
        return false;
    }

    // get the server URL
    public String getServerUrl(final Boolean forDocumentServer) {
        if (forDocumentServer && !docserviceUrlExample.equals("")) {
            return docserviceUrlExample;
        } else {

            HttpServletRequest request = WebUtil.getRequest();
            String selfAddress = InetUtil.getHttpSelfAddress();
            return selfAddress + request.getContextPath();
        }
    }

    // get the history directory
    public String getHistoryDir(final String path) {
        return path + historyPostfix;
    }

    // get the file version
    public int getFileVersion(final String historyPath, final Boolean ifIndexPage) {
        Path path;
        if (Boolean.TRUE.equals(ifIndexPage)) {  // if the start page is opened
            path = Paths.get(getStorageLocation()
                    + getHistoryDir(historyPath));  // get the storage directory and add the history directory to it
        } else {
            path = Paths.get(historyPath);  // otherwise, get the path to the history directory
            if (!Files.exists(path)) {
                return 1;  // if the history directory does not exist, then the file version is 1
            }
        }

        // run through all the files in the history directory
        try (Stream<Path> stream = Files.walk(path, 1)) {
            return stream
                    .filter(Files::isDirectory)  // take only directories from the history folder
                    .map(Path::getFileName)  // get file names
                    .map(Path::toString)  // and convert them into strings
                    .collect(Collectors.toSet()).size();  /* convert stream into set
                     and get its size which specifies the file version */
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
