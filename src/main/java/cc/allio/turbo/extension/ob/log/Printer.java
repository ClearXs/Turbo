package cc.allio.turbo.extension.ob.log;

import cc.allio.uno.core.util.JsonUtils;
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


    /**
     * 根据输入的对象，按照系统的配置进行日志输出
     *
     * @param formatContent 提供slf4j的语法内容进行格式化输出
     * @param source        对象源
     */
    public static void print(String formatContent, Object... source) {
        log.debug(formatContent, JsonUtils.toJson(source));
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
