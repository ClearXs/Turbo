package cc.allio.uno.turbo.extension.oss;

import cc.allio.uno.turbo.extension.log.Printer;
import lombok.extern.slf4j.Slf4j;

/**
 * 提供公用方法
 *
 * @author j.x
 * @date 2023/11/18 10:42
 * @since 0.1.0
 */
@Slf4j
public abstract class BaseOssExecutor implements OssExecutor {

    @Override
    public boolean upload(OssPutRequest ossPutRequest) {
        try {
            boolean result = doUpload(ossPutRequest);
            Printer.print("{} upload {}", getProvider().getValue(), ossPutRequest.getObject(), result);
            return result;
        } catch (Throwable ex) {
            log.error("{} oss upload {} failed", getProvider().getValue(), ossPutRequest.getObject(), ex);
        }
        return false;
    }

    @Override
    public OssResponse download(OssGetRequest ossGetRequest) {
        try {
            OssResponse result = doDownload(ossGetRequest);
            Printer.print("{} download {}", getProvider().getValue(), ossGetRequest, result.getObject());
            return result;
        } catch (Throwable ex) {
            log.error("{} oss download {} failed", getProvider().getValue(), ossGetRequest.getObject(), ex);
        }
        return null;
    }

    /**
     * 子类实现，不用关系异常处理
     */
    protected abstract boolean doUpload(OssPutRequest ossPutRequest) throws Throwable;

    /**
     * 子类实现，不用关系异常处理
     */
    protected abstract OssResponse doDownload(OssGetRequest ossGetRequest) throws Throwable;
}
