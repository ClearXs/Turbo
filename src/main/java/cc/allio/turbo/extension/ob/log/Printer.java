package cc.allio.turbo.extension.ob.log;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统共用组件，日志内容输出
 *
 * @author j.x
 * @date 2023/11/18 10:33
 * @since 0.1.0
 */
@Slf4j
public final class Printer {

    public static void print(String template, Object... args) {
        log.debug(template, args);
    }

    public static void print(LogType logType, String template, Object... args) {

    }

    public static void print(LogType logType, Object obj) {

    }


    /**
     * 输出错误信息
     *
     * @param ex ex
     */
    public static void error(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }
}
