package cc.allio.turbo.common.db.constraint;

import cc.allio.turbo.common.constant.Direction;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 注解于实体字段上，用于标识该字段排序方法
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface Sortable {

    /**
     * 排序方式，默认降序
     */
    Direction direction() default Direction.DESC;
}
