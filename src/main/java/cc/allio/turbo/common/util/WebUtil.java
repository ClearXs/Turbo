package cc.allio.turbo.common.util;

import cc.allio.uno.core.StringPool;
import jakarta.servlet.http.HttpServletRequest;
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

    public static final String AUTHENTICATION = "X-AUTHENTICATION";
    public static final String TENANT = "X-TENANT";

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
        return Optionals.firstNonEmpty(() -> getHeaderOpt(TENANT), () -> getAttributeOpt(TENANT)).orElse(null);
    }

    /**
     * 获取http Request中的token信息
     * <p>follow header, attribute get token</p>
     *
     * @return token
     * @throws NoSuchElementException value empty
     */
    public static String getToken() {
        return Optionals.firstNonEmpty(() -> getHeaderOpt(AUTHENTICATION), () -> getAttributeOpt(AUTHENTICATION)).orElse(StringPool.EMPTY);
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
}
