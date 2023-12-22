package cc.allio.turbo.extension.oss;

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
        String accessKeyId = ossTrait.getAccessId();
        String accessKeySecret = ossTrait.getAccessKey();
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(accessKeyId, accessKeySecret);
        this.ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        this.ossTrait = ossTrait;
    }

    @Override
    protected boolean doUpload(OssPutRequest ossPutRequest) throws Throwable {
        return ossClient.putObject(ossTrait.getBucket(), ossPutRequest.getObject(), ossPutRequest.getInputStream()) != null;
    }

    @Override
    protected OssResponse doDownload(OssGetRequest ossGetRequest) throws Throwable {
        OSSObject ossObject = ossClient.getObject(ossTrait.getBucket(), ossGetRequest.getObject());
        boolean successful = ossObject.getResponse().isSuccessful();

        OssResponse response = new OssResponse();
        response.setSuccessful(successful);
        response.setObject(ossObject.getKey());
        response.setInputStream(ossObject.getObjectContent());

        return response;
    }

    @Override
    public Provider getProvider() {
        return Provider.ALIYUN;
    }

}
