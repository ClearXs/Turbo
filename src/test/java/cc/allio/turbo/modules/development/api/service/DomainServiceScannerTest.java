package cc.allio.turbo.modules.development.api.service;

import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.Parameter;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Set;

@RunTest
public class DomainServiceScannerTest extends BaseTestCase {

    @Test
    void testScanCandidate(@Parameter CoreTest coreTest) {
        GenericApplicationContext applicationContext = coreTest.getContext();
        DomainServiceScanner scanner = new DomainServiceScanner(applicationContext, getClass().getClassLoader());
        Set<DomainBeanDefinition> domainBeanDefinitions = scanner.scanCandidate("cc.allio.turbo.modules.developer.api.service");

        assertEquals(1, domainBeanDefinitions.size());

        DomainBeanDefinition domainBeanDefinition = domainBeanDefinitions.stream().findFirst().get();
        assertEquals(TestDomainServiceImpl.class, domainBeanDefinition.getDomainServiceClass());
        assertEquals(TestDomainObject.class, domainBeanDefinition.getDomainObjectClass());
    }
}
