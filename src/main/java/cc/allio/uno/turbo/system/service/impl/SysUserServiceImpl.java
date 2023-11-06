package cc.allio.uno.turbo.system.service.impl;

import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.util.SecureUtil;
import cc.allio.uno.turbo.system.constant.UserStatus;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.mapper.SysUserMapper;
import cc.allio.uno.turbo.system.service.ISysUserService;
import cc.allio.uno.turbo.system.vo.SysUserVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SecureProperties secureProperties;

    @Override
    @Transactional
    public boolean saveUser(SysUser sysUser) throws BizException {
        // 1.验证是否有相同的用户名（考虑后续手机号、邮箱）
        long count = count(lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()));
        if (count > 0) {
            throw new BizException(String.format("用户%s重复", sysUser.getUsername()));
        }
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        String encryptPassword = secureCipher.encrypt(sysUser.getPassword(), secureProperties.getSecretKey(), null);
        sysUser.setPassword(encryptPassword);
        sysUser.setStatus(UserStatus.ENABLE);
        return save(sysUser);
    }

    @Override
    public SysUserVO findByUsername(String username) {
        SysUser userEntity = getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        SysUserVO user = CoreBeanUtil.copy(userEntity, SysUserVO.class);
        // TODO 1.查找角色信息
        // TODO 2.查找组织信息
        return user;
    }
}
