package cc.allio.turbo.common.db.uno.repository.mybatis;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.core.util.template.internal.TokenParser;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.Wrapper} adaptor
 *
 * @author j.x
 * @date 2024/3/31 15:05
 * @since 0.1.1
 */
public interface WrapperAdaptor<T> {

    TokenParser HASH_BRACE_TOKEN_PARSER = ExpressionTemplate.createParse(Tokenizer.HASH_BRACE);
    TokenParser BRACE_TOKEN_PARSER = ExpressionTemplate.createParse(Tokenizer.DOUBLE_BRACKET);

    /**
     * 基于uno-data的{@link cc.allio.uno.data.orm.dsl.Operator}实例进行适配
     *
     * @param operator operator
     * @return operator
     */
    T adapt(T operator);


    /**
     * 基于{@link TokenParser}在paramNameValuePairs的Map中获取占位符的值
     *
     * @param placeholder         placeholder
     * @param paramNameValuePairs paramNameValuePairs
     * @return value or null
     */
    static Object getValue(String placeholder, Map<String, Object> paramNameValuePairs) {
        if (StringUtils.isBlank(placeholder)) {
            return null;
        }
        AtomicReference<Object> valueRef = new AtomicReference<>(null);
        HASH_BRACE_TOKEN_PARSER.parse(
                placeholder,
                token -> {
                    String symbolise = token.substring(token.lastIndexOf(StringPool.ORIGIN_DOT) + 1);
                    Object v = paramNameValuePairs.get(symbolise);
                    if (v != null) {
                        valueRef.set(v);
                    }
                    return token;
                });
        return valueRef.get();
    }
}
