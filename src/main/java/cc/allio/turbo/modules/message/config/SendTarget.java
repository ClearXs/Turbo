package cc.allio.turbo.modules.message.config;

import lombok.Data;

/**
 * 发送目标
 *
 * @author j.x
 * @date 2024/3/28 23:03
 * @since 0.1.1
 */
@Data
public class SendTarget {

	/**
	 * 用户类型
	 * <p>ORG 组织、GROUP 用户组、ROLE 角色、USER 指定用户、CUSTOM 自定义</p>
	 */
	private Target target;

	/**
	 * 标识
	 */
	private String key;
}
