package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.turbo.common.db.persistent.PersistentProperties;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.uno.core.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Wrapper for {@link HttpSessionOAuth2AuthorizationRequestRepository}.
 * <p>Add tenant to session .</p>
 *
 * @author j.x
 * @date 2024/3/30 22:28
 * @since 0.1.1
 */
public class TenantSessionAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> internal;
    private final PersistentProperties persistentProperties;

    public TenantSessionAuthorizationRequestRepository(PersistentProperties persistentProperties) {
        this.internal = new HttpSessionOAuth2AuthorizationRequestRepository();
        this.persistentProperties = persistentProperties;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return internal.loadAuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        // try to tenant id or '0' if tenant is empty
        String tenantId = request.getHeader(WebUtil.X_TENANT);
        if (StringUtils.isBlank(tenantId)) {
            tenantId = persistentProperties.getTenant().getDefaultTenantId();
        }
        // set tenant to session
        request.getSession().setAttribute(WebUtil.X_TENANT, tenantId);
        internal.saveAuthorizationRequest(authorizationRequest, request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = internal.removeAuthorizationRequest(request, response);
        if (oAuth2AuthorizationRequest != null) {
            // remove and set tenant to attributes
            HttpSession session = request.getSession(false);
            Object tenant = session.getAttribute(WebUtil.X_TENANT);
            request.setAttribute(WebUtil.X_TENANT, tenant);
            session.removeAttribute(WebUtil.X_TENANT);
        }
        return oAuth2AuthorizationRequest;
    }
}
