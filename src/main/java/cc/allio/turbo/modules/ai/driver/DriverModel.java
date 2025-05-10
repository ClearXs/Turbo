package cc.allio.turbo.modules.ai.driver;

import java.lang.annotation.*;

/**
 * annotate {@link Driver} model. allow annotated class to be a driver model.
 *
 * @author j.x
 * @since 0.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DriverModel {
}
