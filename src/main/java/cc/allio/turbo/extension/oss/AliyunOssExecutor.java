package cc.allio.turbo.extension.oss;

import cc.allio.turbo.extension.oss.request.OssGetRequest;
import cc.allio.turbo.extension.oss.request.OssPutRequest;
import cc.allio.turbo.extension.oss.request.OssRemoveRequest;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;

/**
 * aliyun oss execute
 *
 * @author j.x
 * @date 2023/11/17 15:46
 * @since 0.1.0
 */
@Slf4j
public class AliyunOssExecutor extends BaseOssExecutor {

    private final OSS ossClient;
    private final OssTrait ossTrait;

    public AliyunOssExecutor(OssTrait ossTrait) {
        String endpoint = ossTrait.getEndpoint();
        String accessKey = ossTrait.getAccessKey();
        String secretKey = ossTrait.getSecretKey();
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(accessKey, secretKey);
        this.ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        this.ossTrait = ossTrait;
    }

    @Override
    protected Path doUpload(OssPutRequest ossPutRequest) throws Throwable {
        Path path = ossPutRequest.getPath();
        path.withStrategy();
        String baseDir = ossTrait.getBaseDir();
        if (StringUtils.isNotBlank(baseDir)) {
            path.appendFirst(Path.from(baseDir, ossTrait.getStrategy()));
        }
        String object = path.compose();
        ossClient.putObject(ossTrait.getBucket(), object, ossPutRequest.getInputStream());

        String endpoint = ossTrait.getEndpoint();
        String fullPath = endpoint + StringPool.SLASH + object;
        return Path.from(fullPath, object, ossTrait.getStrategy());
    }

    @Override
    protected OssResponse doDownload(OssGetRequest ossGetRequest) throws Throwable {
        Path path = ossGetRequest.getPath();
        String object = path.compose();
        OSSObject ossObject = ossClient.getObject(ossTrait.getBucket(), object);
        boolean successful = ossObject.getResponse().isSuccessful();

        OssResponse response = new OssResponse();
        response.setSuccessful(successful);
        response.setObject(ossObject.getKey());
        response.setInputStream(ossObject.getObjectContent());

        return response;
    }

    @Override
    protected boolean doRemove(OssRemoveRequest ossRemoveRequest) throws Throwable {
        return false;
    }

    @Override
    protected Path doCopyObject(String src, String dest) throws Throwable {
        return null;
    }

    @Override
    public Provider getProvider() {
        return Provider.ALIYUN;
    }

}
