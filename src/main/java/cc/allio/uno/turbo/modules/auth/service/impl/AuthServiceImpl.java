package cc.allio.uno.turbo.modules.auth.service.impl;

import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.turbo.modules.auth.properties.SecureProperties;
import cc.allio.uno.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.uno.turbo.modules.auth.constant.ExpireAt;
import cc.allio.uno.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.uno.turbo.modules.auth.service.IAuthService;
import cc.allio.uno.turbo.modules.auth.provider.TurboUser;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.util.AuthUtil;
import cc.allio.uno.turbo.common.util.JwtUtil;
import cc.allio.uno.turbo.common.cache.Caches;
import cc.allio.uno.turbo.common.cache.TurboCache;
import cc.allio.uno.turbo.common.util.SecureUtil;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.entity.SysRole;
import cc.allio.uno.turbo.modules.system.entity.SysUser;
import cc.allio.uno.turbo.modules.system.param.SysMenuParam;
import cc.allio.uno.turbo.modules.system.service.ISysMenuService;
import cc.allio.uno.turbo.modules.system.service.ISysRoleService;
import cc.allio.uno.turbo.modules.system.service.ISysUserService;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTree;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.springboot.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final ISysUserService sysUserService;
    private final SecureProperties secureProperties;
    private final ISysRoleService roleService;
    private final ISysMenuService menuService;

    @Override
    public CaptchaDTO captcha() {
        SecureProperties.Captcha captchaSettings = secureProperties.getCaptcha();
        SpecCaptcha captcha = new SpecCaptcha(captchaSettings.getWidth(), captchaSettings.getHeight(), captchaSettings.getLength());
        TurboCache turboCache = Caches.getIfAbsent(Caches.CAPTCHA);
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
        sysUserService.save(sysUser);
        // 2.获取新创建用户详细
        SysUserVO user = sysUserService.findByUsername(sysUser.getUsername());
        // 3.生成jwt
        return JwtUtil.encode(new TurboUser(user));
    }

    @Override
    public List<SysMenuTree> currentUserMenus() {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        long userId = currentUser.getUserId();
        // TODO 需要优化该接口的查询逻辑
        List<SysRole> roles = roleService.getRolesByUser(userId);
        List<Long> menuIds = roleService.getRoleMenuIdByIds(roles.stream().map(SysRole::getId).toList());
        // 根据菜单id获取菜单树
        SysMenuParam sysMenuParam = new SysMenuParam();
        sysMenuParam.setMenuIds(menuIds);
        List<SysMenu> expandTree = menuService.tree(sysMenuParam);
        return menuService.treeify(expandTree, SysMenuTree.class);
    }

    @Override
    public TurboJwtAuthenticationToken changePassword(String newPassword) throws BizException {
        String currentUsername = AuthUtil.getCurrentUsername();
        if (StringUtils.isBlank(currentUsername)) {
            throw new BizException(ExceptionCodes.ACCESS_DENIED);
        }
        String encryptPassword = encryptPassword(newPassword);
        boolean update = sysUserService.update(Wrappers.<SysUser>lambdaUpdate().set(SysUser::getPassword, encryptPassword).eq(SysUser::getUsername, currentUsername));
        if (update) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        // 生成新的jwt
        SysUserVO user = sysUserService.findByUsername(currentUsername);
        return JwtUtil.encode(new TurboUser(user));
    }

    @Override
    @Transactional
    public TurboJwtAuthenticationToken modify(TurboUser user) throws BizException {
        SysUser sysUser = CoreBeanUtil.copy(user, SysUser.class);
        if (sysUser == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        sysUser.setId(user.getUserId());
        sysUserService.updateById(sysUser);
        // 生成新的jwt
        SysUserVO newUserInfo = sysUserService.findByUsername(user.getUsername());
        return JwtUtil.encode(new TurboUser(newUserInfo));
    }

    private String encryptPassword(String rawPassword) {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        return secureCipher.encrypt(rawPassword, secureProperties.getSecretKey(), null);
    }
}
