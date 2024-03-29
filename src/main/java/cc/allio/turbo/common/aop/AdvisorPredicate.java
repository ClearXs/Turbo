package cc.allio.turbo.common.aop;

import cc.allio.uno.core.function.lambda.ThrowingMethodPredicate;

/**
 * bean class 进行实现，在判断{@link TurboAdvisorBuilder#allow(Object)}时进行验证，如果验证结果为false则不进行aop代理
 *
 * @author j.x
 * @date 2024/3/5 23:20
 * @since 0.1.1
 */
public interface AdvisorPredicate extends ThrowingMethodPredicate<TurboAdvisorBuilder<?>> {
}
