package cc.allio.turbo.extension.oss;

import cc.allio.turbo.extension.oss.request.OssGetRequest;
import cc.allio.turbo.extension.oss.request.OssPutRequest;
import cc.allio.turbo.extension.oss.request.OssRemoveRequest;

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
     * @return the put result
     */
    Path upload(OssPutRequest ossPutRequest);

    /**
     * 对象下载
     *
     * @param ossGetRequest ossRequest
     * @return OssResponse
     */
    OssResponse download(OssGetRequest ossGetRequest);

    /**
     * object remove
     *
     * @param ossRemoveRequest the {@link OssRemoveRequest} instance
     * @return true if success
     */
    boolean remove(OssRemoveRequest ossRemoveRequest);

    /**
     * copy source object dest to target
     * <p>like as /2024/xx.doc /2024/2/xx.doc</p>
     *
     * @param src  the source path
     * @param dest the dest path
     * @return the object result
     */
    Path copyObject(String src, String dest);

    /**
     * 获取存储商标识
     */
    Provider getProvider();
}
