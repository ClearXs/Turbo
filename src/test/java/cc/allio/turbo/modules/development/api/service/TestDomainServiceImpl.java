package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.modules.development.api.GeneralDomainObject;
import cc.allio.turbo.modules.development.api.annotation.Domain;

@Domain("test")
public class TestDomainServiceImpl extends DeclareDomainCrudTreeRepositoryServiceImpl<TestDomainObject> implements ITestDomainService {

    public TestDomainServiceImpl(IDomainService<GeneralDomainObject> actual, Class<TestDomainObject> domainObjectClass) {
        super(actual, domainObjectClass);
    }
}
