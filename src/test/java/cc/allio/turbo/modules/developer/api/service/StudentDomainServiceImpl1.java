package cc.allio.turbo.modules.developer.api.service;

import cc.allio.turbo.modules.developer.api.GeneralDomainObject;
import cc.allio.turbo.modules.developer.api.annotation.Domain;

@Domain("student")
public class StudentDomainServiceImpl1 extends DeclareDomainCrudTreeRepositoryServiceImpl<StudentDomainObject> implements IStudentDomainService {

    public StudentDomainServiceImpl1(IDomainService<GeneralDomainObject> actual, Class<StudentDomainObject> domainObjectClass) {
        super(actual, domainObjectClass);
    }
}
