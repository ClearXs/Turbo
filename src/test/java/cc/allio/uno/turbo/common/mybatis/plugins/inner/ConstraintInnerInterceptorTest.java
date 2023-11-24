package cc.allio.uno.turbo.common.mybatis.plugins.inner;

import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import cc.allio.uno.turbo.common.mybatis.MybatisConfiguration;
import cc.allio.uno.turbo.common.mybatis.entity.Student;
import cc.allio.uno.turbo.common.mybatis.mapper.StudentMapper;
import cc.allio.uno.turbo.common.persistent.PersistentConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.UncategorizedSQLException;

@RunTest(components = {MybatisConfiguration.class, PersistentConfiguration.class})
@MybatisPlusEnv(basePackages = "cc.allio.uno.turbo.common.mybatis.**")
public class ConstraintInnerInterceptorTest {

    @Inject
    private StudentMapper studentMapper;

    @Test
    void testUnique() {
        Student student1 = new Student();
        student1.setCode("1");

        studentMapper.insert(student1);

        Student student2 = new Student();
        student2.setCode("1");
        Assertions.assertThrows(UncategorizedSQLException.class, () -> studentMapper.insert(student2));

        studentMapper.delete(Wrappers.emptyWrapper());
    }

    @Test
    void testMultiUnique() {
        Student student1 = new Student();
        student1.setCode("1");
        student1.setName("1");

        studentMapper.insert(student1);

        Student student2 = new Student();
        student2.setCode("1");
        student2.setName("1");
        Assertions.assertThrows(UncategorizedSQLException.class, () -> studentMapper.insert(student2));

        studentMapper.delete(Wrappers.emptyWrapper());
    }

    @Test
    void testUniqueByUpdate() {
        Student student = new Student();
        student.setCode("1");
        student.setName("1");
        studentMapper.insert(student);

        Assertions.assertThrows(UncategorizedSQLException.class, () -> studentMapper.updateById(student));
        studentMapper.delete(Wrappers.emptyWrapper());

    }
}
