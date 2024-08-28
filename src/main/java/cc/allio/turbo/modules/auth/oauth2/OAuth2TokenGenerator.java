package cc.allio.turbo.modules.auth.oauth2;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.oauth2.extractor.OAuth2UserExtractor;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.turbo.modules.auth.service.IAuthService;
import cc.allio.turbo.modules.system.constant.UserSource;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.entity.SysThirdUser;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.service.ISysThirdUserService;
import cc.allio.turbo.modules.system.service.ISysUserService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.tx.TransactionContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * generate oauth token base on jwt token.
 * <p>This Class is stateful, creating token will be register new user if user non existing. </p>
 *
 * @author j.x
 * @date 2024/3/30 18:44
 * @since 0.1.1
 */
@AllArgsConstructor
public class OAuth2TokenGenerator {

    private final ISysUserService sysUserService;
    private final ISysThirdUserService sysThirdUserService;
    private final IAuthService authService;
    private final JwtAuthentication jwtAuthentication;
    private final Map<String, OAuth2UserExtractor> extractorMap;

    /**
     * Create JWT Oauth2 token base on {@link OAuth2AuthenticationToken}.
     * <p>it will be read db get sys user if non exist then storage db..</p>
     *
     * @param oAuth2AuthenticationToken oAuth2AuthenticationToken
     * @return a {@link Jwt} instance
     * @throws BizException         create jwt has error
     * @throws NullPointerException registrationId, extractor, uuid is null
     */
    public Jwt createToken(OAuth2AuthenticationToken oAuth2AuthenticationToken) throws BizException {
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        if (StringUtils.isBlank(registrationId)) {
            throw Exceptions.unNull("Oauth2 login client registration id is null");
        }
        OAuth2UserExtractor extractor = extractorMap.get(registrationId);
        if (extractor == null) {
            throw Exceptions.unNull(String.format("Oauth2 user extractor is null by registration id is %s", registrationId));
        }
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        // find third user
        String uuid = extractor.withUUID(oAuth2User);
        if (StringUtils.isBlank(uuid)) {
            throw Exceptions.unNull("Oauth 2 user uuid is null, please check extractor");
        }

        SysThirdUser thirdUser = sysThirdUserService.getOne(Wrappers.lambdaQuery(SysThirdUser.class).eq(SysThirdUser::getUuid, uuid));
        if (thirdUser == null) {
            TurboJwtAuthenticationToken authenticationToken = registerThirdUser(extractor, oAuth2User);
            return authenticationToken.getToken();
        }
        // find sys user
        SysUser sysUser = sysUserService.getById(thirdUser.getUserId());
        if (sysUser == null) {
            TurboJwtAuthenticationToken authenticationToken = registerSysUser(extractor, oAuth2User);
            return authenticationToken.getToken();
        }
        SysUserVO userDetails = sysUserService.findUserDetails(sysUser);
        List<SysRole> roles = userDetails.getRoles();
        Set<TurboGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        TurboJwtAuthenticationToken token = jwtAuthentication.encode(new TurboUser(userDetails, authorities));
        return token.getToken();
    }

    /**
     * base on {@link OAuth2UserExtractor} and {@link OAuth2User} register third and sys user in db
     *
     * @param extractor  the extractor
     * @param oAuth2User the oAuth2User
     * @return a {@link TurboJwtAuthenticationToken} instance
     */
    TurboJwtAuthenticationToken registerThirdUser(OAuth2UserExtractor extractor, OAuth2User oAuth2User) {
        // create third user
        String uuid = extractor.withUUID(oAuth2User);
        var thirdUser = new SysThirdUser();
        thirdUser.setUuid(uuid);
        thirdUser.setCode(extractor.getRegistrationId());
        return TransactionContext.execute(
                () -> {
                    TurboJwtAuthenticationToken token = registerSysUser(extractor, oAuth2User);
                    Long id = token.getUserId();
                    thirdUser.setUserId(id);
                    sysThirdUserService.saveOrUpdate(thirdUser);
                    return token;
                });
    }

    /**
     * register sys user in db
     *
     * @param extractor  the extractor
     * @param oAuth2User the oAuth2User
     * @return a {@link TurboJwtAuthenticationToken} instance
     * @throws BizException create jwt has error
     */
    TurboJwtAuthenticationToken registerSysUser(OAuth2UserExtractor extractor, OAuth2User oAuth2User) throws BizException {
        // register new sys user
        var sysUser = new SysUser();
        sysUser.setSource(UserSource.THIRD);
        String username = extractor.withUsername(oAuth2User);
        sysUser.setUsername(username);
        sysUser.setNickname(username);

        String email = extractor.withEmail(oAuth2User);
        sysUser.setEmail(email);
        return authService.register(sysUser);
    }
}
