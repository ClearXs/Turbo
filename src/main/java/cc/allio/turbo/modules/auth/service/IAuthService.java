package cc.allio.turbo.modules.auth.service;

import cc.allio.turbo.modules.auth.authentication.TurboJwtAuthenticationToken;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.turbo.modules.system.dto.ChangePasswordDTO;
import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.entity.SysPost;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.domain.SysMenuTree;
import cc.allio.turbo.modules.auth.dto.CaptchaDTO;
import cc.allio.turbo.common.exception.BizException;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IAuthService {

    /**
     * 生成验证码
     *
     * @return
     */
    CaptchaDTO captcha();

    /**
     * 注册账号
     *
     * @param sysUser sysUser 实体
     * @return 生成的jwt token
     */
    TurboJwtAuthenticationToken register(SysUser sysUser) throws BizException;

    /**
     * 更改密码
     *
     * @param user the current {@link TurboUser}
     * @param changePassword the {@link ChangePasswordDTO} instance
     * @return 重新生成的jwt token
     */
    TurboJwtAuthenticationToken changePassword(@NotNull TurboUser user, ChangePasswordDTO changePassword) throws BizException;

    /**
     * 修改个人信息
     *
     * @param user user
     * @return 重新生成的jwt token
     */
    TurboJwtAuthenticationToken modify(TurboUser user) throws BizException;

    /**
     * 通用系统的加密算法对密码加密
     *
     * @param rawPassword 明文密码
     * @return 密文
     */
    String encryptPassword(String rawPassword);

    /**
     * from the {@link TurboUser} get user menus
     *
     * @param user the current {@link TurboUser}
     * @return the {@link SysMenuTree} list
     */
    List<SysMenuTree> getUserMenus(@NotNull TurboUser user);

    /**
     * from the {@link TurboUser} get user org
     *
     * @param user the current {@link TurboUser}
     * @return get {@link SysOrg}
     */
    SysOrg getUserOrg(@NotNull TurboUser user);

    /**
     * 获取当前用户角色
     *
     * @param user the current {@link TurboUser}
     * @return the {@link SysRole} list
     */
    List<SysRole> getUserRole(@NotNull TurboUser user);

    /**
     * 获取当前用户岗位
     *
     * @param user the current {@link TurboUser}
     * @return the {@link SysPost} list
     */
    List<SysPost> getUserPost(@NotNull TurboUser user);
}
