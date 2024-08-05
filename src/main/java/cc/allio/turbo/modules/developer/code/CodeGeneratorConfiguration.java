package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import cc.allio.turbo.modules.developer.service.IDevPageService;
import cc.allio.turbo.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class CodeGeneratorConfiguration {

    @Bean
    public PageCodeGenerator pageCodeGenerator(IDevPageService devPageService, IDevBoService devBoService, ISysCategoryService categoryService) {
        return new PageCodeGenerator(devPageService, devBoService, categoryService);
    }

    @Bean
    public BoCodeGenerator boCodeGenerator(IDevBoService devBoService, ISysCategoryService categoryService) {
        return new BoCodeGenerator(categoryService, devBoService);
    }

    @Bean
    public DataTableCodeGenerator dataTableCodeGenerator(IDevDataSourceService dataSourceService, ISysCategoryService categoryService) {
        return new DataTableCodeGenerator(dataSourceService, categoryService);
    }

    @Bean
    public CodeGeneratorManager codeGeneratorManager(ObjectProvider<List<CodeGenerator>> codeGeneratorProvider) {
        CodeGeneratorManager codeGeneratorManager = new CodeGeneratorManager();
        List<CodeGenerator> codeGeneratorList = codeGeneratorProvider.getIfAvailable(Collections::emptyList);
        for (CodeGenerator codeGenerator : codeGeneratorList) {
            codeGeneratorManager.registry(codeGenerator);
        }
        return codeGeneratorManager;
    }
}
