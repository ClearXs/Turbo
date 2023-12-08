package cc.allio.uno.turbo.common.mybatis.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 注解于实体上，对实体的某个字段进行约束，如不能为空、唯一。
 * <p>可以放在同一实体的多个字段上，这些字段将组成唯一性判断</p>
 *
 * @author j.x
 * @date 2023/11/22 15:33
 * @since 0.1.0
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface Unique {

}
