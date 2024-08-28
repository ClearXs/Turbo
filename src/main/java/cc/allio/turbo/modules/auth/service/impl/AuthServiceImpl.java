package cc.allio.turbo.modules.auth.service.impl;

import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.authority.TurboGrantedAuthority;
import cc.allio.turbo.modules.auth.constant.ExpireAt;
import cc.allio.turbo.modules.auth.jwt.JwtAuthentication;
import cc.allio.turbo.modules.auth.properties.SecureProperties;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.turbo.modules.system.dto.ChangePasswordDTO;
import cc.allio.turbo.modules.system.entity.*;
import cc.allio.turbo.modules.system.service.*;
import cc.allio.turbo.modules.system.domain.SysMenuTree;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.turbo.modules.auth.service.IAuthService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.ExceptionCodes;
import cc.allio.turbo.common.cache.CacheHelper;
import cc.allio.turbo.common.cache.TurboCache;
import cc.allio.turbo.common.util.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.springboot.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final ISysUserService userService;
    private final SecureProperties secureProperties;
    private final ISysRoleService roleService;
    private final ISysRoleMenuService roleMenuService;
    private final ISysMenuService menuService;
    private final ISysOrgService orgService;
    private final ISysPostService postService;
    private final JwtAuthentication jwtAuthentication;

    @Override
    public CaptchaDTO captcha() {
        SecureProperties.Captcha captchaSettings = secureProperties.getCaptcha();
        SpecCaptcha captcha = new SpecCaptcha(captchaSettings.getWidth(), captchaSettings.getHeight(), captchaSettings.getLength());
        TurboCache turboCache = CacheHelper.getIfAbsent(CacheHelper.CAPTCHA);
        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setCaptchaId(IdGenerator.defaultGenerator().toHex());
        captchaDTO.setBase64(captcha.toBase64());
        ExpireAt expireAt = captchaSettings.getExpireAt();
        turboCache.setEx(captchaDTO.getCaptchaId(), captcha.text(), expireAt.range(), TimeUnit.MILLISECONDS);
        return captchaDTO;
    }

    @Override
    @Transactional
    public TurboJwtAuthenticationToken register(SysUser sysUser) throws BizException {
        // 1.存入数据库
        String rawPassword = sysUser.getPassword();
        String encryptPassword = encryptPassword(rawPassword);
        sysUser.setPassword(encryptPassword);
        userService.save(sysUser);
        // 2.获取新创建用户详细
        SysUserVO user = userService.findByUsername(sysUser.getUsername());
        List<SysRole> roles = user.getRoles();
        Set<TurboGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        // 3.生成jwt
        return jwtAuthentication.encode(new TurboUser(user, authorities));
    }

    @Override
    public TurboJwtAuthenticationToken changePassword(TurboUser user, ChangePasswordDTO changePassword) throws BizException {
        Long currentUserId = user.getUserId();
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSystemSecureCipher();
        // 1.查询旧密码是否与当前数据库存储一致
        String rawEncryptPassword = secureCipher.encrypt(changePassword.getRawPassword(), SecureUtil.getSystemSecretKey(), null);
        SysUser currentUser = userService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getId, currentUserId).eq(SysUser::getPassword, rawEncryptPassword));
        if (currentUser == null) {
            throw new BizException(ExceptionCodes.USER_RAW_PASSWORD_MISTAKE);
        }
        // 更新密码
        Boolean update = userService.changePassword(currentUserId, changePassword.getNewPassword());
        if (Boolean.FALSE.equals(update)) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        // 生成新的jwt
        SysUserVO detailsUser = userService.details(currentUserId);
        List<SysRole> roles = detailsUser.getRoles();
        Set<TurboGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        return jwtAuthentication.encode(new TurboUser(detailsUser, authorities));
    }

    @Override
    @Transactional
    public TurboJwtAuthenticationToken modify(TurboUser user) throws BizException {
        SysUser sysUser = BeanUtils.copy(user, SysUser.class);
        if (sysUser == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        sysUser.setId(user.getUserId());
        userService.updateById(sysUser);
        // 生成新的jwt
        SysUserVO newUserInfo = userService.findByUsername(user.getUsername());
        List<SysRole> roles = newUserInfo.getRoles();
        Set<TurboGrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new TurboGrantedAuthority(role.getId(), role.getCode(), role.getName()))
                        .collect(Collectors.toSet());
        return jwtAuthentication.encode(new TurboUser(newUserInfo, authorities));
    }

    @Override
    public String encryptPassword(String rawPassword) {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        return secureCipher.encrypt(rawPassword, secureProperties.getSecretKey(), null);
    }

    @Override
    public List<SysMenuTree> getUserMenus(TurboUser user) {
        long userId = user.getUserId();
        List<SysRole> roles = roleService.findRolesByUserId(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        List<Long> roleIds = roles.stream().map(SysRole::getId).toList();
        List<SysRoleMenu> sysRoleMenus = roleMenuService.list(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds));
        List<Long> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).toList();
        // 获取菜单树
        return menuService.tree(Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getId, menuIds), SysMenuTree.class);
    }

    @Override
    public SysOrg getUserOrg(TurboUser user) {
        SysUser sysUser = userService.getById(user.getUserId());
        if (sysUser != null) {
            Long orgId = sysUser.getOrgId();
            return orgService.getById(orgId);
        }
        return null;
    }

    @Override
    public List<SysRole> getUserRole(TurboUser user) {
        Long currentUserId = user.getUserId();
        return roleService.findRolesByUserId(currentUserId);
    }

    @Override
    public List<SysPost> getUserPost(TurboUser user) {
        Long currentUserId = user.getUserId();
        return postService.findPostByUserId(currentUserId);
    }
}
