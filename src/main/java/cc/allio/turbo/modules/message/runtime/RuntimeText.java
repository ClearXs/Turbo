package cc.allio.turbo.modules.message.runtime;

/**
 * 运行时文本数据
 *
 * @author j.x
 * @date 2024/3/28 23:58
 * @since 0.1.1
 */
public interface RuntimeText {

	/**
	 * 获取执行脚本解析运行后文本数据
	 *
	 * @return 解析后的文本数据
	 * @throws NullPointerException 给定的变量数据为null抛出异常
	 */
	String runThenText();

	/**
	 * 获取原始文本信息
	 *
	 * @return 原始文本信息
	 */
	String getText();
}
