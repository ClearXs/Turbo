package cc.allio.turbo.common.db.uno;

import cc.allio.turbo.common.db.uno.repository.WrapperAdapter;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.SPIOperatorHelper;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.Data;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@RunTest(active = "repository")
@MybatisPlusEnv
public class WrapperAdapterTest extends BaseTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    public void init() {
        Configuration configuration = sqlSessionFactory.getConfiguration();

        MapperBuilderAssistant mock = Mockito.mock(MapperBuilderAssistant.class);

        Mockito.when(mock.getConfiguration()).thenReturn(configuration);
        Mockito.when(mock.getCurrentNamespace()).thenReturn(WrapperAdapterTest.User.class.getName());
        TableInfoHelper.initTableInfo(mock, WrapperAdapterTest.User.class);
    }

    @Test
    void testWhereEQ() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").eq("a1", "xx");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 = 'xx'", dsl);
    }

    @Test
    void testWhereNE() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").ne("a1", "xx");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 != 'xx'", dsl);
    }

    @Test
    void testWhereIn() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").in("a2", "a", "b", "c");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a2 IN ('a', 'b', 'c')", dsl);
    }

    @Test
    void testWhereNotIn() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").notIn("a2", "a", "b", "c");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a2 NOT IN ('a', 'b', 'c')", dsl);
    }

    @Test
    void testWhereIsNull() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").isNull("a1");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 IS NULL\n", dsl);
    }

    @Test
    void testWhereIsNotNull() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").isNotNull("a1");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 IS NOT NULL\n", dsl);
    }

    @Test
    void testWhereBetween() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").between("a1", "a", "b");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 BETWEEN 'a' AND 'b'", dsl);
    }

    @Test
    void testWhereNotBetween() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").notBetween("a1", "a", "b");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 NOT BETWEEN 'a' AND 'b'", dsl);
    }

    @Test
    void testWhereLikeLeft() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").likeLeft("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 LIKE '%a'", dsl);
    }

    @Test
    void testWhereNotLikeLeft() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").notLikeLeft("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 NOT LIKE '%a'", dsl);
    }

    @Test
    void testWhereLikeRight() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").likeRight("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 LIKE 'a%'", dsl);
    }

    @Test
    void testWhereNotLikeRight() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").notLikeRight("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 NOT LIKE 'a%'", dsl);
    }

    @Test
    void testWhereLike() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").like("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 LIKE '%a%'", dsl);
    }

    @Test
    void testWhereNotLike() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").notLike("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 NOT LIKE '%a%'", dsl);
    }

    @Test
    void testWhereLT() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").lt("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 < 'a'", dsl);
    }

    @Test
    void testWhereLE() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").le("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 <= 'a'", dsl);
    }

    @Test
    void testWhereGT() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").gt("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 > 'a'", dsl);
    }

    @Test
    void testWhereGE() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2").ge("a1", "a");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 >= 'a'", dsl);
    }

    @Test
    void testWhereComplex() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2")
                .eq("a1", "xx")
                .in("a2", "a", "b")
                .isNotNull("x")
                .between("a1", "a", "b")
                .likeLeft("x", "x")
                .eq("asd", "asd");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE a1 = 'xx'\n" +
                "\tAND a2 IN ('a', 'b')\n" +
                "\tAND x IS NOT NULL\n" +
                "\tAND a1 BETWEEN 'a' AND 'b'\n" +
                "\tAND x LIKE '%x'\n" +
                "\tAND asd = 'asd'", dsl);
    }

    @Test
    void testWhereLogic() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2")
                .likeLeft("x", "x")
                .or()
                .eq("asd", "asd");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "WHERE x LIKE '%x'\n" +
                "\tOR asd = 'asd'", dsl);
    }

    @Test
    void testOrder() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2")
                .orderByAsc("asd")
                .orderByDesc("asd");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "\n" +
                "ORDER BY asd ASC, asd DESC", dsl);
    }

    @Test
    void testGroup() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(User.class);
        queryWrapper.select("a1", "a2")
                .groupBy("asd");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT a1, a2\n" +
                "FROM PUBLIC.User\n" +
                "GROUP BY asd", dsl);
    }

    @Test
    void testLambdaWhere() {
        QueryOperator queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>(User.class);
        queryWrapper.select(User::getName)
                .eq(User::getName, "21")
                .eq(false, User::getName, "asd")
                .le(User::getName, "asd");
        WrapperAdapter.adapt(queryWrapper, queryOperator);
        String dsl = queryOperator.getDSL();
        assertEquals("SELECT name\n" +
                "FROM PUBLIC.User\n" +
                "WHERE name = '21'\n" +
                "\tAND name <= 'asd'", dsl);
    }

    @Test
    void testUpdate() {
        UpdateOperator updateOperator = SPIOperatorHelper.lazyGet(UpdateOperator.class, OperatorKey.SQL);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setEntityClass(User.class);
        updateWrapper.set("a1", "a1").set("a2", "a2").eq("a1", "as").ge("a2", "123");
        WrapperAdapter.adapt(updateWrapper, updateOperator);
        String dsl = updateOperator.getDSL();
        assertEquals("UPDATE PUBLIC.User\n" +
                "SET a1 = 'a1', a2 = 'a2'\n" +
                "WHERE a1 = 'as'\n" +
                "\tAND a2 >= '123'", dsl);
    }

    @Data
    @TableName("user")
    public static class User {

        @TableField("name")
        private String name;
    }

}
