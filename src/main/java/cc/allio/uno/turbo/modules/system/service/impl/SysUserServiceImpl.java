package cc.allio.uno.turbo.modules.system.service.impl;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.turbo.common.i18n.LocaleFormatter;
import cc.allio.uno.turbo.common.mybatis.help.Conditions;
import cc.allio.uno.turbo.common.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.i18n.ExceptionCodes;
import cc.allio.uno.turbo.common.util.SecureUtil;
import cc.allio.uno.turbo.common.web.params.EntityTerm;
import cc.allio.uno.turbo.common.web.params.QueryParam;
import cc.allio.uno.turbo.modules.auth.properties.SecureProperties;
import cc.allio.uno.turbo.modules.system.constant.UserStatus;
import cc.allio.uno.turbo.modules.system.dto.BindingOrgDTO;
import cc.allio.uno.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.uno.turbo.modules.system.entity.*;
import cc.allio.uno.turbo.modules.system.mapper.SysUserMapper;
import cc.allio.uno.turbo.modules.system.service.*;
import cc.allio.uno.turbo.modules.system.vo.SysUserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends TurboCrudServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ISysRoleService roleService;
    private final ISysUserRoleService userRoleService;
    private final ISysOrgService sysOrgService;
    private final ISysUserOrgService userOrgService;
    private final SecureProperties secureProperties;

    @Override
    @Transactional
    public boolean saveOrUpdate(SysUser sysUser) {
        long count = count(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()));
        if (count > 0) {
            throw new RuntimeException(LocaleFormatter.getMessage(ExceptionCodes.USER_REPEAT));
        }
        sysUser.setStatus(UserStatus.ENABLE);
        return super.saveOrUpdate(sysUser);
    }

    @Override
    public List<SysUser> findList(QueryParam<SysUser> param) {
        QueryWrapper<SysUser> queryWrapper = Conditions.query(param, SysUser.class);
        EntityTerm orgTerm = param.getTerm(SysUserOrg::getOrgId);
        if (orgTerm != null) {
            QueryWrapper<SysUserOrg> userOrgQueryWrapper = Wrappers.query();
            Conditions.TermCondition cond = Conditions.getCondByField(orgTerm.getEntityField());
            cond.doCondition(orgTerm, userOrgQueryWrapper);
            List<Long> userIds = userOrgService.list(userOrgQueryWrapper).stream().map(SysUserOrg::getUserId).distinct().toList();
            if (orgTerm != null && orgTerm.getValue() != null) {
                return Collections.emptyList();
            }
            queryWrapper.in("id", userIds);
        }
        return list(queryWrapper);
    }

    @Override
    public IPage<SysUser> findPage(QueryParam<SysUser> param) {
        QueryWrapper<SysUser> queryWrapper = Conditions.query(param, SysUser.class);
        EntityTerm orgTerm = param.getTerm(SysUserOrg::getOrgId);
        if (orgTerm != null && orgTerm.getValue() != null) {
            QueryWrapper<SysUserOrg> userOrgQueryWrapper = Wrappers.query();
            Conditions.TermCondition cond = Conditions.getCondByField(orgTerm.getEntityField());
            cond.doCondition(orgTerm, userOrgQueryWrapper);
            List<Long> userIds = userOrgService.list(userOrgQueryWrapper).stream().map(SysUserOrg::getUserId).distinct().toList();
            if (CollectionUtils.isEmpty(userIds)) {
                return param.getPage();
            }
            queryWrapper.in("id", userIds);
        }
        return page(param.getPage(), queryWrapper);
    }

    @Override
    public SysUserVO findByUsername(String username) throws BizException {
        SysUser userEntity = getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BizException(ExceptionCodes.USER_NOT_FOUND);
        }
        SysUserVO user = CoreBeanUtil.copy(userEntity, SysUserVO.class);
        if (user == null) {
            throw new BizException(ExceptionCodes.OPERATE_ERROR);
        }
        List<SysRole> roles = roleService.getRolesByUser(user.getId());
        user.setRoles(roles);
        List<SysOrg> orgs = sysOrgService.getOrgByUserId(user.getId());
        user.setOrgs(orgs);
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

    @Override
    public Boolean changePassword(Long userId, String newPassword) {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSecureCipher(secureProperties.getSecureAlgorithm());
        String encryptPassword = secureCipher.encrypt(newPassword, secureProperties.getSecretKey(), null);
        return update(Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getPassword, encryptPassword));
    }

    @Override
    @Transactional
    public Boolean bingdingOrgs(BindingOrgDTO bindingOrg) {
        // 移除之前绑定的组织数据
        userOrgService.remove(Wrappers.<SysUserOrg>lambdaQuery().eq(SysUserOrg::getUserId, bindingOrg.getUserId()));
        List<SysUserOrg> sysUserOrgs = bindingOrg.getOrgIds().stream()
                .map(orgId -> {
                    SysUserOrg sysUserOrg = new SysUserOrg();
                    sysUserOrg.setUserId(bindingOrg.getUserId());
                    sysUserOrg.setOrgId(orgId);
                    return sysUserOrg;
                })
                .toList();
        return userOrgService.saveBatch(sysUserOrgs);
    }

    @Override
    public List<SysUser> unboundOrgUser() {
        List<Long> bindingUserIds = userOrgService.list().stream().map(SysUserOrg::getUserId).distinct().toList();
        return list(Wrappers.<SysUser>lambdaQuery().notIn(SysUser::getId, bindingUserIds));

    }
}
