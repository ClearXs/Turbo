package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.modules.development.api.GeneralDomainObject;
import cc.allio.turbo.modules.development.api.annotation.Domain;

@Domain("student")
public class StudentDomainServiceImpl1 extends DeclareDomainCrudTreeRepositoryServiceImpl<StudentDomainObject> implements IStudentDomainService {

    public StudentDomainServiceImpl1(IDomainService<GeneralDomainObject> actual, Class<StudentDomainObject> domainObjectClass) {
        super(actual, domainObjectClass);
    }
}
