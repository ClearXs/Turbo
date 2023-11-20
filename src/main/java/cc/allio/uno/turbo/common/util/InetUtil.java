package cc.allio.uno.turbo.common.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络地址工具类
 *
 * @author j.x
 * @date 2023/11/18 12:56
 * @since 1.0.0
 */
@Slf4j
public final class InetUtil {


    /**
     * 获取当前系统的Http协议下的系统地址
     *
     * @return http://127.0.0.1:8600 ... maybe null
     */
    public static String getHttpSelfAddress() {
        String selfAddress = Envs.getProperty("file.upload.path", String.class);
        if (StringUtils.isBlank(selfAddress)) {
            return selfAddress;
        }
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            String hostAddress = netAddress.getHostAddress();
            Integer serverPort = Envs.getProperty("server.port", Integer.class);
            if (serverPort == null) {
                selfAddress = StringPool.HTTP + hostAddress;
            } else {
                selfAddress = StringPool.HTTP + hostAddress + StringPool.COLON + serverPort;
            }
        } catch (UnknownHostException ex) {
            log.error("can't fetch host", ex);
        }
        return selfAddress;
    }

}
