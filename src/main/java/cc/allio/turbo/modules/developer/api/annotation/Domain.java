package cc.allio.turbo.modules.developer.api.annotation;

import cc.allio.turbo.modules.developer.api.service.DomainServiceRegistry;
import cc.allio.turbo.modules.developer.api.service.DomainServiceScanner;
import cc.allio.turbo.modules.developer.api.service.IDomainService;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * declare which one class or interface become be provided with domain behavior, mead any method become behavior method，
 * from {@link DomainServiceRegistry} management, and possess inspect for any behavior method , and subscribe-publish modal any domain method.
 *
 * <ul>
 *     <li>可以用于继承{@link IDomainService}的接口上</li>
 *     <li>可以用于实现{@link IDomainService}的类上</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/2/27 17:29
 * @see DomainServiceScanner
 * @see DomainServiceRegistry
 * @since 0.1.1
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface Domain {

    /**
     * 领域对象唯一标识
     */
    String value();
}
