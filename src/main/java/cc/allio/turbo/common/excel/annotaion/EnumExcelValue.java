package cc.allio.turbo.common.excel.annotaion;

import java.lang.annotation.*;

/**
 * @author h.x
 * @date 2023/12/19 15:07
 * @since 0.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface EnumExcelValue {
}
