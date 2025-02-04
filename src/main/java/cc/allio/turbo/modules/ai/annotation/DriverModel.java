package cc.allio.turbo.modules.ai.annotation;

import cc.allio.turbo.modules.ai.Driver;

import java.lang.annotation.*;

/**
 * annotate {@link Driver} model.
 *
 * @author j.x
 * @since 0.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DriverModel {
}
