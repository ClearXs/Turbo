package cc.allio.turbo.modules.message.template;

import cc.allio.turbo.modules.message.entity.SysMessageTemplate;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DefaultMessageTemplate implements MessageTemplate {

    private final String templateKey;
    private final Extension extension;
    private final List<Variable> variables;
    /**
     * 文本数据
     */
    private final Map<String, String> texts;

    public DefaultMessageTemplate(SysMessageTemplate messageTemplate) {
        // 模板#{}表达式模板解析器
        this.templateKey = messageTemplate.getKey();
        this.extension = messageTemplate.getExtension();
        this.variables = messageTemplate.getVariables();
        this.texts = Maps.newHashMap();
        texts.put(MessageTemplate.TITLE, messageTemplate.getTitle());
        texts.put(MessageTemplate.TEMPLATE, messageTemplate.getTemplate());
    }

    @Override
    public String getKey() {
        return templateKey;
    }

    @Override
    public Extension getExtension() {
        return extension;
    }

    @Override
    public List<Variable> getVariable() {
        return variables;
    }

    @Override
    public String getText(String key) {
        return texts.getOrDefault(key, "");
    }
}
