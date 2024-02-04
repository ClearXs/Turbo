package cc.allio.turbo.common.util;

import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class VariationAnalyzerTest extends BaseTestCase {

    @Test
    void testLessSize() {
        ArrayList<String> s1 = Lists.newArrayList("1", "2");
        ArrayList<String> b1 = Lists.newArrayList();
        VariationAnalyzer<String, String> a1 = new VariationAnalyzer<>(s1, b1, String::toString, (o1, o2) -> !o1.equals(o2));

        assertDoesNotThrow(a1::analyze);

        ArrayList<String> s2 = Lists.newArrayList();
        ArrayList<String> b2 = Lists.newArrayList();
        VariationAnalyzer<String, String> a2 = new VariationAnalyzer<>(s2, b2, String::toString, (o1, o2) -> !o1.equals(o2));
        assertDoesNotThrow(a2::analyze);
    }

    @Test
    void testBasicType() {
        ArrayList<String> sources = Lists.newArrayList("1", "2");
        ArrayList<String> benchmark = Lists.newArrayList("2", "3");
        VariationAnalyzer<String, String> analyzer = new VariationAnalyzer<>(sources, benchmark, String::toString, (o1, o2) -> !o1.equals(o2));
        VariationAnalyzer.AnalyzeResultSet<String, String> result = analyzer.analyze();
        List<VariationAnalyzer.Result<String, String>> addition = result.getAddition();
        List<VariationAnalyzer.Result<String, String>> reduction = result.getReduction();
        List<VariationAnalyzer.Result<String, String>> mutative = result.getMutative();

        assertEquals(1, addition.size());

        assertEquals(1, reduction.size());
        assertEquals(0, mutative.size());
    }

    @Test
    void testCompositeType() {
        Student s1 = new Student("s1", 1);
        Student s2 = new Student("s2", 1);
        Student s3 = new Student("s3", 1);
        Student s4 = new Student("s4", 2);

        ArrayList<Student> sources = Lists.newArrayList(s1, s3, s4);
        ArrayList<Student> benchmark = Lists.newArrayList(s2, s3, s4);
        VariationAnalyzer<Student, String> analyzer = new VariationAnalyzer<>(sources, benchmark, Student::getName, (o1, o2) -> !o1.equals(o2));
        VariationAnalyzer.AnalyzeResultSet<Student, String> result = analyzer.analyze();
        List<VariationAnalyzer.Result<Student, String>> addition = result.getAddition();
        List<VariationAnalyzer.Result<Student, String>> reduction = result.getReduction();
        List<VariationAnalyzer.Result<Student, String>> mutative = result.getMutative();

        assertEquals(1, addition.size());
        assertEquals(s1, addition.get(0).getSource());

        assertEquals(1, reduction.size());
        assertEquals(s2, reduction.get(0).getBench());

        assertEquals(0, mutative.size());

    }

    @Test
    void testThrowNull() {
        Student s1 = new Student(null, 1);
        ArrayList<Student> sources = Lists.newArrayList(s1);
        ArrayList<Student> benchmark = Lists.newArrayList();

        VariationAnalyzer<Student, String> analyzer = new VariationAnalyzer<>(sources, benchmark, Student::getName, (o1, o2) -> !o1.equals(o2));
        assertThrows(NullPointerException.class, analyzer::analyze);
    }

    @Data
    @AllArgsConstructor
    public static class Student {
        private final String name;

        private final Integer age;

    }
}
