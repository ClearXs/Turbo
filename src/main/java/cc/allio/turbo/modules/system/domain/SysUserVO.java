package cc.allio.turbo.modules.system.domain;

import cc.allio.turbo.modules.system.entity.SysOrg;
import cc.allio.turbo.modules.system.entity.SysPost;
import cc.allio.turbo.modules.system.entity.SysRole;
import cc.allio.turbo.modules.system.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * sys user值对象
 *
 * @author j.x
 * @date 2023/10/22 12:23
 * @since 0.1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVO extends SysUser {

    /**
     * 角色信息
     */
    @Schema(description = "角色信息")
    private List<SysRole> roles;

    /**
     * 组织信息
     */
    @Schema(description = "组织信息")
    private SysOrg org;

    /**
     * 岗位信息
     */
    @Schema(description = "岗位信息")
    private List<SysPost> posts;
}
