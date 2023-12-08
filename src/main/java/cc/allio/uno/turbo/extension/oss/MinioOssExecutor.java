package cc.allio.uno.turbo.extension.oss;

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
    public Provider getProvider() {
        return Provider.MINIO;
    }

}
