package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainServiceRegistryTest extends BaseTestCase {

    DomainServiceRegistry domainServiceRegistry;

    @Override
    public void onInit() throws Throwable {
        BoSchema mockSchema1 = new BoSchema();
        mockSchema1.setCode("1");
        mockSchema1.setMaterialize(true);
        mockSchema1.setDataSourceId(1L);

        BoSchema mockSchema2 = new BoSchema();
        mockSchema2.setCode("test");
        mockSchema2.setMaterialize(true);
        mockSchema2.setDataSourceId(1L);

        BoSchema mockSchema3 = new BoSchema();
        mockSchema3.setCode("student");
        mockSchema3.setMaterialize(true);
        mockSchema3.setDataSourceId(1L);


        IDevBoService devBoService = Mockito.mock(IDevBoService.class);
        Mockito.when(devBoService.cacheToSchema("1")).thenReturn(mockSchema1);
        Mockito.when(devBoService.cacheToSchema("test")).thenReturn(mockSchema2);
        Mockito.when(devBoService.cacheToSchema("student")).thenReturn(mockSchema3);

        AggregateCommandExecutor commandExecutor = Mockito.mock(AggregateCommandExecutor.class);
        IDevDataSourceService dataSourceService = Mockito.mock(IDevDataSourceService.class);
        Mockito.when(dataSourceService.getCommandExecutor(1L)).thenReturn(commandExecutor);

        domainServiceRegistry = new DomainServiceRegistryImpl(devBoService, dataSourceService, null);
    }

    @Test
    void testRegisterDirectly() {
        assertDoesNotThrow(() -> {
            IDomainService<GeneralDomainObject> d1 = domainServiceRegistry.registerDirectly("1");
            IDomainService<GeneralDomainObject> d2 = domainServiceRegistry.getDomainService("1");
            assertEquals(d1, d2);
        });
    }

    @Test
    void testRegisterDeclarative() {
        assertThrows(NullPointerException.class, () -> domainServiceRegistry.registerDeclarative(StudentDomainObject.class));

        assertDoesNotThrow(() -> {
            IDomainService<TestDomainObject> d1 = domainServiceRegistry.registerDeclarative(TestDomainObject.class);
            IDomainService<TestDomainObject> d2 = domainServiceRegistry.getDomainService(TestDomainObject.class);
            assertEquals(d1, d2);
        });

        assertThrows(NullPointerException.class, () -> {
            domainServiceRegistry.registerDeclarative(StudentDomainObject.class, IStudentDomainService.class);
        });

        assertDoesNotThrow(() -> {
            ITestDomainService d1 = domainServiceRegistry.registerDeclarative(TestDomainObject.class, ITestDomainService.class);
            IDomainService<TestDomainObject> d2 = domainServiceRegistry.getDomainService(TestDomainObject.class);
            IDomainService<GeneralDomainObject> d3 = domainServiceRegistry.getDomainService("test");
            assertEquals(d1, d2);
            assertNotNull(d3);
            BoSchema boSchema = d1.getBoSchema();
            assertEquals("test", boSchema.getCode());
        });

        assertDoesNotThrow(() -> {
            IStudentDomainService d1 = domainServiceRegistry.registerDeclarative(StudentDomainObject.class, StudentDomainServiceImpl1.class);
            IStudentDomainService d2 = domainServiceRegistry.getDomainService(StudentDomainObject.class, StudentDomainServiceImpl1.class);
            assertEquals(d1, d2);
            BoSchema boSchema = d1.getBoSchema();
            assertEquals("student", boSchema.getCode());
        });
    }
}
