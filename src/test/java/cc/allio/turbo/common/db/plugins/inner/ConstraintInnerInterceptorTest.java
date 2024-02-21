package cc.allio.turbo.common.db.plugins.inner;

import cc.allio.turbo.common.db.entity.Student;
import cc.allio.turbo.common.db.mapper.StudentMapper;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import cc.allio.turbo.common.db.mybatis.TurboMybatisConfiguration;
import cc.allio.turbo.common.db.persistent.PersistentConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.UncategorizedSQLException;

@RunTest(components = {TurboMybatisConfiguration.class, PersistentConfiguration.class})
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
