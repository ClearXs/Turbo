package cc.allio.turbo.common.db.mybatis.help;

import cc.allio.uno.core.util.StringUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.reflect.Field;

/**
 * Mybatis以及mybatis-plus相关操作的工具类
 *
 * @author jiangwei
 * @date 2023/12/7 17:11
 * @since 0.1.0
 */
public class MybatisKit {

    private MybatisKit() {
    }

    /**
     * 获取数据库表的字段名称
     */
    public static String getTableColumn(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            return tableField.value();
        }
        return StringUtils.camelToUnderline(field.getName());
    }
}
