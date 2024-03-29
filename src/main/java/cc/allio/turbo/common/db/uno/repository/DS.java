package cc.allio.turbo.common.db.uno.repository;

import cc.allio.turbo.common.db.constant.StorageType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation on {@link ITurboRepository} and it's subtype
 *
 * @author j.x
 * @date 2024/3/28 23:18
 * @since 0.1.1
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface DS {

    StorageType value();
}
