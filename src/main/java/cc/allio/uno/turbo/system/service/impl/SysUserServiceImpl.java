package cc.allio.uno.turbo.system.service.impl;

import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.util.SecureUtil;
import cc.allio.uno.turbo.system.constant.UserStatus;
import cc.allio.uno.turbo.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.system.entity.SysRole;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.entity.SysUserRole;
import cc.allio.uno.turbo.system.mapper.SysUserMapper;
import cc.allio.uno.turbo.system.service.ISysRoleService;
import cc.allio.uno.turbo.system.service.ISysUserRoleService;
import cc.allio.uno.turbo.system.service.ISysUserService;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SecureProperties secureProperties;
    private final ISysRoleService roleService;
    private final ISysUserRoleService userRoleService;

    @Override
    @Transactional
    public boolean saveUser(SysUser sysUser) throws BizException {
        // 1.验证是否有相同的用户名（考虑后续手机号、邮箱）
        long count = count(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()));
        if (count > 0) {
            throw new BizException(ExceptionCodes.USER_REPEAT);
        }
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        String encryptPassword = secureCipher.encrypt(sysUser.getPassword(), secureProperties.getSecretKey(), null);
        sysUser.setPassword(encryptPassword);
        sysUser.setStatus(UserStatus.ENABLE);
        return save(sysUser);
    }

    @Override
    public SysUserVO findByUsername(String username) throws BizException {
        SysUser userEntity = getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BizException(ExceptionCodes.USER_NOT_FOUND);
        }
        SysUserVO user = CoreBeanUtil.copy(userEntity, SysUserVO.class);
        List<SysRole> roles = roleService.getRolesByUser(user.getId());
        user.setRoles(roles);
        // TODO 2.查找组织信息
        return user;
    }

    @Override
    @Transactional
    public Boolean bindingRoles(BindingRoleDTO bindingRole) {
        // 多次绑定，需要删除之前绑定的数据
        userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, bindingRole.getUserId()));
        List<SysUserRole> userRoles = bindingRole.getRoleIds()
                .stream()
                .map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(bindingRole.getUserId());
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .toList();
        return userRoleService.saveBatch(userRoles);
    }
}
