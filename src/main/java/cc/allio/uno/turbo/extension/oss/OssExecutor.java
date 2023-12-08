package cc.allio.uno.turbo.extension.oss;

/**
 * oss execute
 *
 * @author j.x
 * @date 2023/11/17 15:44
 * @since 0.1.0
 */
public interface OssExecutor {

    /**
     * 对象上传
     *
     * @param ossPutRequest ossPutRequest
     * @return true or false
     */
    boolean upload(OssPutRequest ossPutRequest);

    /**
     * 对象下载
     *
     * @param ossGetRequest ossRequest
     * @return OssResponse
     */
    OssResponse download(OssGetRequest ossGetRequest);

    /**
     * 获取存储商标识
     */
    Provider getProvider();
}
