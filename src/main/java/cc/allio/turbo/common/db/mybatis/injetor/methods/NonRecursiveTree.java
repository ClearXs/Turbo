package cc.allio.turbo.common.db.mybatis.injetor.methods;

import cc.allio.turbo.common.db.entity.TreeEntity;
import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * non-recursive tree select
 *
 * @author j.x
 * @date 2024/8/1 13:31
 * @see TreeMapper#selectNonRecursiveTree(Wrapper)
 * @since 0.1.1
 */
public class NonRecursiveTree extends AbstractMethod {

    // 1.%s 初始项
    // 2.%s 递归项
    // 3.%s 最后递归查询数据字典
    private static final String TREE_SQL = "<script> WITH RECURSIVE biz_tree AS (%s) SELECT %s FROM biz_tree %s </script>";

    public NonRecursiveTree() {
        super("selectNonRecursiveTree");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 校验
        if (!TreeMapper.class.isAssignableFrom(mapperClass) && !TreeEntity.class.isAssignableFrom(modelClass)) {
            return null;
        }
        // select <columns> from dual where ... order ...
        String sqlList = String.format("SELECT %s FROM %s %s %s %s", sqlSelectColumns(tableInfo, true), tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        String sql = String.format(TREE_SQL, sqlList, sqlSelectColumns(tableInfo, true), treeWhere(tableInfo));
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
    }

    /**
     * 构造树查询 where 条件
     *
     * @param tableInfo tableInfo
     * @return
     */
    private String treeWhere(TableInfo tableInfo) {
        if (tableInfo.isWithLogicDelete()) {
            String where = tableInfo.getLogicDeleteSql(true, true);
            return SqlScriptUtils.convertWhere(where);
        } else {
            return StringPool.EMPTY;
        }
    }
}
