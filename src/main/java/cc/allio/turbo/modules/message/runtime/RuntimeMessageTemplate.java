package cc.allio.turbo.modules.message.runtime;

import cc.allio.turbo.modules.message.template.Extension;
import cc.allio.turbo.modules.message.template.MessageTemplate;
import cc.allio.turbo.modules.message.template.Variable;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * 运行时消息模板，增强{@link MessageTemplate}，为其赋予{@link RuntimeText}内容
 *
 * @author j.x
 * @date 2024/3/28 23:58
 * @since 0.1.1
 */
public class RuntimeMessageTemplate implements MessageTemplate {

    private final MessageTemplate messageTemplate;
    private final ExpressionTemplate expressionTemplate;
    private final RuntimeVariable runtimeVariable;
    private final RuntimeText titleText;
    @Getter
    private final RuntimeText contentText;

    static final Extension DEFAULT_EXTENSION = new Extension();

    static {
        DEFAULT_EXTENSION.setAppUrl(StringPool.EMPTY);
        DEFAULT_EXTENSION.setPcUrl(StringPool.EMPTY);
    }

    public RuntimeMessageTemplate(MessageTemplate messageTemplate, RuntimeVariable runtimeVariable) {
        this.expressionTemplate = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE);
        this.messageTemplate = messageTemplate;
        this.runtimeVariable = runtimeVariable;
        this.titleText = getRuntimeText(messageTemplate.getText(MessageTemplate.TITLE));
        this.contentText = getRuntimeText(messageTemplate.getText(MessageTemplate.TEMPLATE));
    }

    @Override
    public String getKey() {
        return messageTemplate.getKey();
    }

    @Override
    public Extension getExtension() {
        return Optional.ofNullable(messageTemplate.getExtension()).orElse(DEFAULT_EXTENSION);
    }

    @Override
    public List<Variable> getVariable() {
        return messageTemplate.getVariable();
    }

    @Override
    public String getText(String key) {
        return messageTemplate.getText(key);
    }

    public RuntimeText getTitleTxt() {
        return titleText;
    }

    public RuntimeText getRuntimeText(String text) {
        return new PlaceholderRuntimeText(text, expressionTemplate, runtimeVariable);
    }

    /**
     * 获取运行时消息变量
     *
     * @return 运行时消息变量
     */
    public RuntimeVariable getRuntimeVariables() {
        return runtimeVariable;
    }
}
