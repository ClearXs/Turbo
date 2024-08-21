package cc.allio.turbo.extension.oss;

import com.google.common.collect.Maps;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * oss execute 静态工厂
 *
 * @author j.x
 * @date 2023/11/17 15:47
 * @since 0.1.0
 */
public class OssExecutorFactory {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * -- GETTER --
     * 获取当前 oss执行器
     *
     * @return maybe null if not exist
     */
    private static OssExecutor current;

    private static final Map<String, Function<OssTrait, OssExecutor>> executors = Maps.newHashMap();

    private static volatile LazyResource lazyLoader;

    static {
        // 阿里云
        executors.put("ALIYUN", AliyunOssExecutor::new);
        // MINIO
        executors.put("MINIO", MinioOssExecutor::new);
    }

    /**
     * 获取当前系统oss执行器
     */
    public static OssExecutor getCurrent() {
        if (current == null && lazyLoader != null) {
            OssTrait resource = lazyLoader.get();
            toggleExecutor(resource);
        }
        return current;
    }

    public static void setLazyLoader(LazyResource lazyLoader) {
        OssExecutorFactory.lazyLoader = lazyLoader;
    }

    /**
     * 根据传入的类型获取执行器实例
     *
     * @return maybe null if not exist
     */
    public static OssExecutor getExecute(OssTrait ossTrait) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Provider provider = ossTrait.getProvider();
            return Optional.ofNullable(provider)
                    .map(Provider::getValue)
                    .map(executors::get)
                    .map(func -> func.apply(ossTrait))
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 切换oss执行器
     */
    public static void toggleExecutor(OssTrait ossTrait) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            OssExecutorFactory.current = getExecute(ossTrait);
        } finally {
            writeLock.unlock();
        }
    }
}
