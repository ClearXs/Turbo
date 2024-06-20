package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.dto.CodeFile;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.entity.DevPage;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.developer.service.IDevCodeGeneratorTemplateService;
import cc.allio.turbo.modules.developer.service.IDevPageService;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.turbo.modules.system.service.ISysCategoryService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DevCodeGenerateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevCodeGenerate> implements IDevCodeGenerateService {

    private final IDevCodeGeneratorTemplateService codeGeneratorTemplateService;
    private final IDevPageService pageService;
    private final IDevBoService devBoService;


    @Override
    public List<CodeFile> preview(Long id) throws BizException {
        DevCodeGenerate codeGenerate = details(id);
        if (codeGenerate == null) {
            throw new BizException("Not found code generate entity by id");
        }

        CodeGenerateSource source = codeGenerate.getSource();

        if (CodeGenerateSource.DATASET == source) {
            DevPage page = pageService.getById(codeGenerate.getPageId());
            Long boId = page.getBoId();


        }

        Long templateCategoryId = codeGenerate.getTemplateCategoryId();
        List<DevCodeGenerateTemplate> devCodeGenerateTemplates =
                codeGeneratorTemplateService.list(Wrappers.lambdaQuery(DevCodeGenerateTemplate.class).eq(DevCodeGenerateTemplate::getCategoryId, templateCategoryId));
        return devCodeGenerateTemplates.stream()
                .map(template -> {
                    CodeFile codeFile = new CodeFile();
                    String filename = template.getFileName() + StringPool.DOT + template.getLanguage().getValue();
                    codeFile.setFilename(filename);
                    codeFile.setTemplateId(template.getId());


                    return codeFile;
                })
                .toList();
    }

    @Override
    public void generate(Long id, HttpServletResponse response) {

    }
}
