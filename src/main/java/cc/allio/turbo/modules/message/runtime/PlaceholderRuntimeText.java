package cc.allio.turbo.modules.message.runtime;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;

/**
 * 占位符运行时文本
 *
 * @author j.x
 * @date 2024/3/28 23:59
 * @since 0.1.1
 */
public class PlaceholderRuntimeText implements RuntimeText {

    private final String originText;
    private final ExpressionTemplate expressionTemplate;
    private final RuntimeVariable runtimeVariable;

    public PlaceholderRuntimeText(String originText, RuntimeVariable runtimeVariable) {
        this(originText, ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE), runtimeVariable);
    }

    public PlaceholderRuntimeText(String originText, ExpressionTemplate expressionTemplate, RuntimeVariable runtimeVariable) {
        this.originText = originText;
        this.expressionTemplate = expressionTemplate;
        this.runtimeVariable = runtimeVariable;
    }

    @Override
    public String thenText() {
        return expressionTemplate.parseTemplate(originText, runtimeVariable.getVariables());
    }

    @Override
    public String getText() {
        return originText;
    }
}
