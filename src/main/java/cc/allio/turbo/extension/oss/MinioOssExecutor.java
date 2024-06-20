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
                .credentials(ossTrait.getAccessId(), ossTrait.getAccessKey())
                .build();
        this.ossTrait = ossTrait;
    }

    @Override
    protected boolean doUpload(OssPutRequest ossPutRequest) throws Throwable {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(ossTrait.getBucket())
                .object(ossPutRequest.getObject())
                // 不进断点续传
                .stream(ossPutRequest.getInputStream(), -1, Integer.MAX_VALUE)
                .build();
        return minioClient.putObject(putObjectArgs) != null;
    }

    @Override
    protected OssResponse doDownload(OssGetRequest ossGetRequest) throws Throwable {
        GetObjectResponse response =
                minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(ossTrait.getBucket())
                                .object(ossGetRequest.getObject())
                                .build()
                );
        OssResponse ossResponse = new OssResponse();
        ossResponse.setObject(response.object());
        ossResponse.setSuccessful(true);
        ossResponse.setInputStream(response);
        return ossResponse;
    }

    @Override
    protected boolean doRemove(OssRemoveRequest ossRemoveRequest) throws Throwable {
        String bucket = ossTrait.getBucket();
        RemoveObjectArgs removeObjectArgs =
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(ossRemoveRequest.getObject())
                        .build();
        minioClient.removeObject(removeObjectArgs);
        return true;
    }

    @Override
    protected String doCopyObject(String src, String dest) throws Throwable {
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
        return dest;
    }

    @Override
    public Provider getProvider() {
        return Provider.MINIO;
    }

}
