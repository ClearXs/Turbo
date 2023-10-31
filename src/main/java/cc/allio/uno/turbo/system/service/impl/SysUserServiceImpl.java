package cc.allio.uno.turbo.system.service.impl;

import cc.allio.uno.turbo.auth.SecureProperties;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.util.SecureUtil;
import cc.allio.uno.turbo.system.entity.SysUser;
import cc.allio.uno.turbo.system.mapper.SysUserMapper;
import cc.allio.uno.turbo.system.service.ISysUserService;
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
        // 1.验证账号、邮箱、手机号三者是否唯一
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        String encryptPassword = secureCipher.encrypt(sysUser.getPassword(), secureProperties.getSecretKey(), null);
        SysUser existUser = findByUsername(sysUser.getUsername());
        if (existUser != null) {
            throw new BizException(String.format("用户%s重复", sysUser.getUsername()));
        }
        sysUser.setPassword(encryptPassword);
        return save(sysUser);
    }

    @Override
    public SysUser findByUsername(String username) {
        return getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
    }
}
