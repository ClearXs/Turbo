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
    public Path upload(OssPutRequest ossPutRequest, OssProperties ossProperties) {
        try {
            Path path = doUpload(ossPutRequest, ossProperties);
            Printer.print("{} upload", getProvider().getValue(), ossPutRequest.getPath());
            return path;
        } catch (Throwable ex) {
            log.error("{} oss upload {} failed", getProvider().getValue(), ossPutRequest.getPath(), ex);
        }
        return null;
    }

    @Override
    public OssResponse download(OssGetRequest ossGetRequest, OssProperties ossProperties) {
        try {
            OssResponse result = doDownload(ossGetRequest, ossProperties);
            Printer.print("{} download {}", getProvider().getValue(), ossGetRequest, result.getObject());
            return result;
        } catch (Throwable ex) {
            log.error("{} oss download {} failed", getProvider().getValue(), ossGetRequest.getPath(), ex);
        }
        return null;
    }

    @Override
    public Path copyObject(String src, String dest, OssProperties ossProperties) {
        try {
            Path path = doCopyObject(src, dest, ossProperties);
            Printer.print("{} copy object src {} to {}", getProvider().getValue(), src, dest);
            return path;
        } catch (Throwable ex) {
            log.error("{} oss copy object failed", getProvider().getValue(), ex);
        }
        return null;
    }

    @Override
    public boolean remove(OssRemoveRequest ossRemoveRequest, OssProperties ossProperties) {
        try {
            boolean result = doRemove(ossRemoveRequest, ossProperties);
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
    protected abstract Path doUpload(OssPutRequest ossPutRequest, OssProperties ossProperties) throws Throwable;

    /**
     * 子类实现，不用关系异常处理
     */
    protected abstract OssResponse doDownload(OssGetRequest ossGetRequest, OssProperties ossProperties) throws Throwable;

    /**
     * subclass implementation
     */
    protected abstract boolean doRemove(OssRemoveRequest ossRemoveRequest, OssProperties ossProperties) throws Throwable;

    /**
     * subclass implementation
     */
    protected abstract Path doCopyObject(String src, String dest, OssProperties ossProperties) throws Throwable;

}
