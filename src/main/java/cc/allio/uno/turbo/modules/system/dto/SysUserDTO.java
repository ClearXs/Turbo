package cc.allio.uno.turbo.modules.system.dto;

import cc.allio.uno.turbo.modules.system.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys user数据传输对象
 *
 * @author j.x
 * @date 2023/10/22 12:24
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDTO extends SysUser {
}
