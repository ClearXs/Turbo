package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.modules.development.domain.TableColumns;
import cc.allio.uno.core.util.template.ExpressionTemplate;

/**
 * according to {@link TableColumns} instance parse to entity template
 *
 * @author j.x
 * @date 2024/5/3 16:02
 * @since 0.1.1
 */
public class CodeTemplateParser {

    static ExpressionTemplate expressionTemplate = ExpressionTemplate.createMVEL();

    /**
     * parse specifies string template.
     *
     * @param template     the string template
     * @param tableColumns the parse template params
     * @return template
     */
    public static synchronized String parse(String template, CodeGenerateContext context) {
        return expressionTemplate.parseTemplate(template, context);
    }
}
