package cc.allio.turbo.extension.oss;

import cc.allio.turbo.extension.oss.request.OssPutRequest;
import cc.allio.uno.core.util.FileUtils;
import cc.allio.uno.core.util.ResourceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class MinioOssExecutorTest {

    MinioOssExecutor minioOssExecutor;

    @BeforeEach
    void setup() {
        var ossTrait = new OssTrait();
        ossTrait.setEndpoint("http://192.168.5.201:8800");
        ossTrait.setBucket("turbo");
        ossTrait.setAccessKey("admin");
        ossTrait.setSecretKey("123456789");
        minioOssExecutor = new MinioOssExecutor(ossTrait);
    }

    @Test
    void testUpload() throws FileNotFoundException {
        FileUtils.FileReadResult fileReadResult = FileUtils.readSingleClassFileForce("logback.xml");
        String content = fileReadResult.getContent();

        OssPutRequest ossPutRequest =
                OssPutRequest.builder()
                        .path(Path.from("file.txt"))
                        .inputStream(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)))
                        .build();

        Path upload = minioOssExecutor.upload(ossPutRequest, new OssProperties());
        Assertions.assertNotNull(upload);
    }
}

