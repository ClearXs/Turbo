package cc.allio.turbo.modules.system.wrapper;

import cc.allio.turbo.extension.oss.OssTrait;
import cc.allio.turbo.extension.oss.Provider;
import cc.allio.turbo.modules.system.entity.SysStorageConfig;
import jakarta.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author j.x
 * @date 2024/8/19 10:18
 * @since 0.1.1
 */
public class StorageConfigWrapper {

    /**
     *
     * {@link SysStorageConfig} wrapper to {@link OssTrait}
     *
     * @param storageConfig the {@link SysStorageConfig} instance
     * @return the {@link OssTrait} instance
     */
    public static OssTrait wrapperToOssTrait(@NotNull SysStorageConfig storageConfig) {
        OssTrait ossTrait = new OssTrait();
        Provider provider = storageConfig.getProvider();
        String name = storageConfig.getName();
        String accessKey = storageConfig.getAccessKey();
        String secretKey = storageConfig.getSecretKey();
        String bucket = storageConfig.getBucket();
        String endpoint = storageConfig.getEndpoint();
        ossTrait.setProvider(provider);
        ossTrait.setApplication(name);
        ossTrait.setAccessKey(accessKey);
        ossTrait.setSecretKey(secretKey);
        ossTrait.setBucket(bucket);
        ossTrait.setEndpoint(endpoint);
        return ossTrait;
    }
}
