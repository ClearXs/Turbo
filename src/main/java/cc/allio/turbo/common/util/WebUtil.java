package cc.allio.turbo.common.util;

import cc.allio.turbo.common.web.App;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.util.Optionals;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * web快捷操作的工具类
 *
 * @author j.x
 * @date 2023/10/27 15:24
 * @see cc.allio.turbo.modules.auth.oauth2.TenantSessionAuthorizationRequestRepository
 * @since 0.1.0
 */
public final class WebUtil extends org.springframework.web.util.WebUtils {

    public static final String X_AUTHENTICATION = "X-AUTHENTICATION";
    public static final String X_TENANT = "X-TENANT";
    public static final String X_LOGIN_MODE = "X-LOGIN-MODE";
    public static final String X_APP = "X-APP";

    private WebUtil() {
    }

    /**
     * 获取http request中的租户号
     * <p>follow header, attribute get tenant</p>
     *
     * @return tenant or null
     * @throws NoSuchElementException value empty
     */
    public static String getTenant() {
        return Optionals.firstNonEmpty(() -> getHeaderOpt(X_TENANT), () -> getAttributeOpt(X_TENANT)).orElse(null);
    }

    /**
     * 获取http Request中的token信息
     * <p>follow header, attribute get token</p>
     *
     * @return token
     * @throws NoSuchElementException value empty
     */
    public static String getToken() {
        return Optionals.firstNonEmpty(() -> getHeaderOpt(X_AUTHENTICATION), () -> getAttributeOpt(X_AUTHENTICATION)).orElse(StringPool.EMPTY);
    }

    /**
     * get login mode
     */
    public static String getLoginMode() {
        return Optionals.firstNonEmpty(() -> getHeaderOpt(X_LOGIN_MODE), () -> getAttributeOpt(X_LOGIN_MODE)).orElse(StringPool.EMPTY);
    }

    /**
     * get app
     */
    public static App getApp() {
        String app = Optionals.firstNonEmpty(() -> getHeaderOpt(X_APP), () -> getAttributeOpt(X_APP)).orElse(StringPool.EMPTY);
        if (StringUtils.isNotBlank(app)) {
            return App.valueOf(app);
        }
        return null;
    }

    /**
     * 获取http request中的header信息
     *
     * @param headerName headerName
     * @return maybe empty
     */
    public static Optional<String> getHeaderOpt(String headerName) {
        return Optional.ofNullable(getRequest()).map(request -> request.getHeader(headerName));
    }

    /**
     * 获取http request中的header信息
     *
     * @param headerName headerName
     * @return maybe empty
     * @throws NoSuchElementException value empty
     */
    public static String getHeader(String headerName) {
        return getHeaderOpt(headerName).orElseThrow();
    }

    /**
     * get http request attributes by attribute name
     *
     * @param attributeName the attributeName
     * @param <T>           the value type
     * @return value instance or null
     */
    public static <T> Optional<T> getAttributeOpt(String attributeName) {
        return Optional.ofNullable(getRequest())
                .map(request -> request.getAttribute(attributeName))
                .map(v -> (T) v);
    }

    /**
     * get http request attributes by attribute name
     *
     * @param attributeName the attributeName
     * @param <T>           the value type
     * @return value instance or null
     * @throws NoSuchElementException value empty
     */
    public static <T> T getAttribute(String attributeName) {
        return (T) getAttributeOpt(attributeName).orElseThrow();
    }

    /**
     * 获取请求中的参数
     *
     * @param paramName 参数名称
     * @return maybe empty
     */
    public static Optional<String> getParameterOpt(String paramName) {
        return Optional.ofNullable(getRequest()).map(request -> request.getParameter(paramName));
    }

    /**
     * 获取请求中的参数
     *
     * @param paramName 参数名称
     * @return maybe empty
     * @throws NoSuchElementException value empty
     */
    public static String getParameter(String paramName) {
        return getParameterOpt(paramName).orElseThrow();
    }

    /**
     * 获取 HttpServletRequest instance
     *
     * @return maybe null
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 从http请求头中获取客户端的ip地址
     *
     * @param request {@link HttpServletRequest} 实例
     * @return ip for {@link String}
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String sourceIp = null;
        String ipAddresses = request.getHeader("x-forwarded-for");
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }
        if (!StringUtils.isEmpty(ipAddresses)) {
            sourceIp = ipAddresses.split(",")[0];
        }
        return sourceIp;
    }

    /**
     * 从{@link HttpSession}中获取session id
     *
     * @param request {@link HttpServletRequest} 实例
     * @return session id for {@link String}
     */
    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getId();
    }
}
