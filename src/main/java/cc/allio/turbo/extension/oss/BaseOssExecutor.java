package cc.allio.turbo.extension.oss;

import cc.allio.turbo.extension.ob.log.Printer;
import cc.allio.turbo.extension.oss.request.OssGetRequest;
import cc.allio.turbo.extension.oss.request.OssPutRequest;
import cc.allio.turbo.extension.oss.request.OssRemoveRequest;
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

    @Override
    public String copyObject(String src, String dest) {
        try {
            String result = doCopyObject(src, dest);
            Printer.print("{} copy object src {} to {}", getProvider().getValue(), src, dest);
            return result;
        } catch (Throwable ex) {
            log.error("{} oss copy object failed", getProvider().getValue(), ex);
        }
        return null;
    }

    @Override
    public boolean remove(OssRemoveRequest ossRemoveRequest) {
        try {
            boolean result = doRemove(ossRemoveRequest);
            Printer.print("{} remove {}", getProvider().getValue(), ossRemoveRequest);
            return result;
        } catch (Throwable ex) {
            log.error("{} oss remove {} failed", getProvider().getValue(), ossRemoveRequest, ex);
        }
        return false;
    }

    /**
     * 子类实现，不用关系异常处理
     */
    protected abstract boolean doUpload(OssPutRequest ossPutRequest) throws Throwable;

    /**
     * 子类实现，不用关系异常处理
     */
    protected abstract OssResponse doDownload(OssGetRequest ossGetRequest) throws Throwable;

    /**
     * subclass implementation
     */
    protected abstract boolean doRemove(OssRemoveRequest ossRemoveRequest) throws Throwable;

    /**
     * subclass implementation
     */
    protected abstract String doCopyObject(String src, String dest) throws Throwable;

}
