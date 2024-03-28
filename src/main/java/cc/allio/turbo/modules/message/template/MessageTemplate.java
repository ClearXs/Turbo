package cc.allio.turbo.modules.message.template;

import java.util.List;

/**
 * 消息模板
 *
 * @author j.x
 * @date 2024/3/28 23:57
 * @since 0.1.1
 */
public interface MessageTemplate {

    String TITLE = "title";
    String TEMPLATE = "template";

    /**
     * 获取配置的模板Key
     *
     * @return
     */
    String getKey();

    /**
     * 获取模板拓展配置
     *
     * @return 拓展配置实体
     */
    Extension getExtension();

    /**
     * 获取变量列表
     *
     * @return 变量列表
     */
    List<Variable> getVariable();

    /**
     * 根据文本关键字获取文本信息
     *
     * @return 文本信息
     */
    String getText(String key);

}
