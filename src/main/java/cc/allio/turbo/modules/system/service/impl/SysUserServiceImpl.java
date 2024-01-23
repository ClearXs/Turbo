package cc.allio.turbo.modules.system.service.impl;

import cc.allio.turbo.modules.system.entity.*;
import cc.allio.turbo.modules.system.service.*;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.turbo.common.i18n.LocaleFormatter;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.ExceptionCodes;
import cc.allio.turbo.common.util.SecureUtil;
import cc.allio.turbo.modules.system.constant.UserStatus;
import cc.allio.turbo.modules.system.dto.BindingOrgDTO;
import cc.allio.turbo.modules.system.dto.BindingPostDTO;
import cc.allio.turbo.modules.system.dto.BindingRoleDTO;
import cc.allio.turbo.modules.system.mapper.SysUserMapper;
import cc.allio.turbo.modules.system.domain.SysUserVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends TurboCrudServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ISysRoleService roleService;
    private final ISysUserRoleService userRoleService;
    private final ISysOrgService orgService;
    private final ISysPostService postService;
    private final ISysUserPostService userPostService;

    @Override
    @Transactional
    public boolean saveOrUpdate(SysUser sysUser) {
        long count = count(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()));
        if (count > 0) {
            throw new RuntimeException(LocaleFormatter.getMessage(ExceptionCodes.USER_REPEAT.getKey()));
        }
        // 新增
        if (sysUser.getId() == null) {
            sysUser.setStatus(UserStatus.ENABLE);
            SecureUtil.SecureCipher secureCipher = SecureUtil.getSystemSecureCipher();
            String password = sysUser.getPassword();
            String encryptPassword = secureCipher.encrypt(password, SecureUtil.getSystemSecretKey(), null);
            sysUser.setPassword(encryptPassword);
        }
        return super.saveOrUpdate(sysUser);
    }


    @Override
    public SysUserVO findByUsername(String username) throws BizException {
        SysUser userEntity = getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BizException(ExceptionCodes.USER_NOT_FOUND);
        }
        return findUserDetails(userEntity);
    }

    @Override
    public SysUserVO details(Long id) {
        SysUser sysUser = super.details(id);
        return findUserDetails(sysUser);
    }

    @Override
    @Transactional
    public Boolean bindingRole(BindingRoleDTO bindingRole) {
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
    public Boolean changePassword(Long userId, String newPassword) throws BizException {
        SecureUtil.SecureCipher secureCipher = SecureUtil.getSystemSecureCipher();
        String encryptPassword = secureCipher.encrypt(newPassword, SecureUtil.getSystemSecretKey(), null);
        return update(
                Wrappers.<SysUser>lambdaUpdate()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getPassword, encryptPassword));
    }

    @Override
    @Transactional
    public Boolean bindingOrg(BindingOrgDTO bindingOrg) {
        return update(
                Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getOrgId, bindingOrg.getOrgId())
                        .eq(SysUser::getId, bindingOrg.getUserId()));
    }

    @Override
    public Boolean bindingPost(BindingPostDTO bindingPost) {
        // 多次绑定，需要删除之前绑定的数据
        userPostService.remove(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, bindingPost.getUserId()));
        List<SysUserPost> userPosts = bindingPost.getPostIds()
                .stream()
                .map(postId -> {
                    SysUserPost userPost = new SysUserPost();
                    userPost.setUserId(bindingPost.getUserId());
                    userPost.setPostId(postId);
                    return userPost;
                })
                .toList();
        return userPostService.saveBatch(userPosts);
    }

    /**
     * 查找用户详细信息
     *
     * @param sysUser sysUser
     * @return SysUserVO
     */
    private SysUserVO findUserDetails(SysUser sysUser) {
        SysUserVO userVO = BeanUtils.copy(sysUser, SysUserVO.class);
        // 1.查找组织信息
        if (sysUser.getOrgId() != null) {
            SysOrg org = orgService.getById(sysUser.getOrgId());
            userVO.setOrg(org);
        }
        // 2.查找岗位信息
        List<SysPost> sysPosts = postService.findPostByUserId(sysUser.getId());
        userVO.setPosts(sysPosts);
        // 3.查找角色信息
        List<SysRole> sysRoles = roleService.findRolesByUserId(sysUser.getId());
        userVO.setRoles(sysRoles);
        return userVO;
    }
}
