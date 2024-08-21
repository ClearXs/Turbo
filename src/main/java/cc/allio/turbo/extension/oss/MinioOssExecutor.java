package cc.allio.turbo.extension.oss;

import cc.allio.turbo.extension.oss.request.OssGetRequest;
import cc.allio.turbo.extension.oss.request.OssPutRequest;
import cc.allio.turbo.extension.oss.request.OssRemoveRequest;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

/**
 * minio execute
 *
 * @author j.x
 * @date 2023/11/17 15:46
 * @since 0.1.0
 */
@Slf4j
public class MinioOssExecutor extends BaseOssExecutor {

    private final MinioClient minioClient;
    private final OssTrait ossTrait;

    public MinioOssExecutor(OssTrait ossTrait) {
        this.minioClient = MinioClient.builder()
                .endpoint(ossTrait.getEndpoint())
                .credentials(ossTrait.getAccessKey(), ossTrait.getSecretKey())
                .build();
        this.ossTrait = ossTrait;
    }

    @Override
    protected Path doUpload(OssPutRequest ossPutRequest, OssProperties ossProperties) throws Throwable {
        Path path = ossPutRequest.getPath();
        String baseDir = ossProperties.getBaseDir();
        path.withStrategy().appendFirst(Path.from(baseDir, ossProperties.getStrategy()));
        String object = path.compose();
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(ossTrait.getBucket())
                .object(object)
                // 不进断点续传
                .stream(ossPutRequest.getInputStream(), -1, Integer.MAX_VALUE)
                .build();
        if (minioClient.putObject(putObjectArgs) != null) {
            return Path.from(object);
        }
        return null;
    }

    @Override
    protected OssResponse doDownload(OssGetRequest ossGetRequest, OssProperties ossProperties) throws Throwable {
        Path path = ossGetRequest.getPath();
        String object = path.compose();
        GetObjectResponse response =
                minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(ossTrait.getBucket())
                                .object(object)
                                .build()
                );
        OssResponse ossResponse = new OssResponse();
        ossResponse.setObject(response.object());
        ossResponse.setSuccessful(true);
        ossResponse.setInputStream(response);
        return ossResponse;
    }

    @Override
    protected boolean doRemove(OssRemoveRequest ossRemoveRequest, OssProperties ossProperties) throws Throwable {
        String bucket = ossTrait.getBucket();
        RemoveObjectArgs removeObjectArgs =
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(ossRemoveRequest.getPath().compose())
                        .build();
        minioClient.removeObject(removeObjectArgs);
        return true;
    }

    @Override
    protected Path doCopyObject(String src, String dest, OssProperties ossProperties) throws Throwable {
        String bucket = ossTrait.getBucket();
        CopyObjectArgs copyObjectArgs = CopyObjectArgs
                .builder()
                .bucket(bucket)
                .source(CopySource.builder().bucket(bucket).object(src).build())
                .object(dest)
                .build();
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.copyObject(copyObjectArgs);
            if (log.isInfoEnabled()) {
                log.info("copy source path={} to destination path={}, the result is {}", src, dest, objectWriteResponse.object());
            }
        } catch (Throwable ex) {
            log.error("Failed copy source filepath={} to destination filepath={}", src, dest, ex);
        }
        return Path.from(dest, ossProperties.getStrategy());
    }

    @Override
    public Provider getProvider() {
        return Provider.MINIO;
    }
}
