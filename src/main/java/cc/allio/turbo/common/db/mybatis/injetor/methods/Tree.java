package cc.allio.turbo.common.db.mybatis.injetor.methods;

import cc.allio.turbo.common.db.mybatis.mapper.TreeMapper;
import cc.allio.turbo.common.db.entity.TreeEntity;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 树查询
 *
 * @author j.x
 * @date 2023/11/27 18:34
 * @see TreeEntity
 * @since 0.1.0
 */
public class Tree extends AbstractMethod {

    // 1.%s 初始项
    // 2.%s 递归项
    // 3.%s 最后递归查询数据字典
    private static final String TREE_SQL = "<script> WITH RECURSIVE biz_tree AS (%s UNION (SELECT sub.* FROM ((%s) sub INNER JOIN biz_tree P ON P.ID = sub.parent_id))) SELECT %s FROM biz_tree %s </script>";

    public Tree() {
        super("selectTree");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 校验
        if (!TreeMapper.class.isAssignableFrom(mapperClass) && !TreeEntity.class.isAssignableFrom(modelClass)) {
            return null;
        }
        // select <columns> from dual where ... order ...
        String sqlList = String.format("SELECT %s FROM %s %s %s %s", sqlSelectColumns(tableInfo, true), tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        String selectFrom = String.format("SELECT %s FROM %s", sqlSelectColumns(tableInfo, true), tableInfo.getTableName());
        String sql = String.format(TREE_SQL, sqlList, selectFrom, sqlSelectColumns(tableInfo, true), treeWhere(tableInfo));
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
