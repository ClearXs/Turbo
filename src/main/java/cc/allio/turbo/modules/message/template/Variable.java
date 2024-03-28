package cc.allio.turbo.modules.message.template;

import lombok.Data;

@Data
public class Variable {

	/**
	 * 变量Key
	 */
	private String key;

	/**
	 * 描述
	 */
	private String des;

	/**
	 * 默认值
	 */
	private String defaultValue;
}
