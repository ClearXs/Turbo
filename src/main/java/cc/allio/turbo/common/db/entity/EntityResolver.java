package cc.allio.turbo.common.db.entity;

import cc.allio.uno.data.orm.dsl.helper.ColumnDefListResolver;
import cc.allio.uno.data.orm.dsl.helper.ColumnDefResolver;
import cc.allio.uno.data.orm.dsl.helper.PojoResolver;
import cc.allio.uno.data.orm.dsl.helper.TableResolver;

/**
 * mybatis-plus 实体解析器
 *
 * @author jiangwei
 * @date 2024/2/6 23:57
 * @since 0.1.0
 */
public interface EntityResolver extends PojoResolver {

    TableResolver TABLE_RESOLVER = new EntityTableResolver();
    ColumnDefListResolver COLUMN_DEF_LIST_RESOLVER = new EntityColumnDefListResolver();
    ColumnDefResolver COLUMN_DEF_RESOLVER = new EntityColumnDefResolver();

    @Override
    default TableResolver obtainTableResolver() {
        return TABLE_RESOLVER;
    }

    @Override
    default ColumnDefListResolver obtainColumnDefListResolver() {
        return COLUMN_DEF_LIST_RESOLVER;
    }

    @Override
    default ColumnDefResolver obtainColumnDefResolver() {
        return COLUMN_DEF_RESOLVER;
    }

}
