package cc.allio.turbo.common.util;

import cc.allio.uno.core.StringPool;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * web快捷操作的工具类
 *
 * @author j.x
 * @date 2023/10/27 15:24
 * @since 0.1.0
 */
public final class WebUtil extends org.springframework.web.util.WebUtils {

    public static final String Authentication = "X-Authentication";
    public static final String Tenant = "X-Tenant";

    private WebUtil() {
    }

    /**
     * 获取http request中的租户号
     *
     * @return tenant or empty
     */
    public static String getTenant() {
        return getHeader(Tenant);
    }

    /**
     * 获取http Request中的token信息
     *
     * @return token
     */
    public static String getToken() {
        return getHeader(Authentication);
    }

    /**
     * 获取http request中的header信息
     *
     * @param headerName headerName
     * @return maybe empty
     */
    public static String getHeader(String headerName) {
        return Optional.ofNullable(getRequest())
                .map(request -> request.getHeader(headerName))
                .orElse(StringPool.EMPTY);
    }

    /**
     * 获取请求中的参数
     *
     * @param paramName 参数名称
     * @return maybe empty
     */
    public static String getParameter(String paramName) {
        return Optional.ofNullable(getRequest())
                .map(request -> request.getParameter(paramName))
                .orElse(StringPool.EMPTY);
    }

    /**
     * 获取 HttpServletRequest instance
     *
     * @return maybe null
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
