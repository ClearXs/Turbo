package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.code.CodeGenerator;
import cc.allio.turbo.modules.developer.code.CodeGeneratorManager;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.developer.service.IDevCodeGeneratorTemplateService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletResponse;
import jodd.io.FileUtil;
import jodd.io.IOUtil;
import jodd.io.ZipBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
public class DevCodeGenerateServiceImpl extends SimpleTurboCrudRepositoryServiceImpl<DevCodeGenerate> implements IDevCodeGenerateService {

    private final IDevCodeGeneratorTemplateService codeGeneratorTemplateService;
    private final CodeGeneratorManager codeGeneratorManager;

    @Override
    public List<CodeContent> preview(Long id) throws BizException {
        DevCodeGenerate codeGenerate = details(id);
        if (codeGenerate == null) {
            throw new BizException("Not found code generate entity by id");
        }
        return doGenerate(codeGenerate);
    }

    @Override
    public void generate(Long id, HttpServletResponse response) throws BizException {
        DevCodeGenerate codeGenerate = details(id);
        if (codeGenerate == null) {
            throw new BizException("Not found code generate entity by id");
        }
        String zipFilename = codeGenerate.getModuleName() + ".zip";
        File zipFile;
        try {
            ZipBuilder zipBuilder = ZipBuilder.createZipFile(new File(zipFilename));
            List<CodeContent> codeContents = doGenerate(codeGenerate);
            for (CodeContent codeContent : codeContents) {
                File codeFile = new File(codeContent.getFilename());
                FileUtil.writeBytes(codeFile, codeContent.getContent().getBytes());
                try {
                    zipBuilder = zipBuilder.add(codeFile).save();
                } finally {
                    FileUtil.delete(codeFile);
                }
            }
            zipFile = zipBuilder.toZipFile();
        } catch (Throwable ex) {
            throw new BizException(ex.getMessage());
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipFilename, StandardCharsets.UTF_8));
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        try (var fileInputStream = new FileInputStream(zipFile)) {
            IOUtil.copy(fileInputStream, response.getOutputStream());
        } catch (Throwable ex) {
            throw new BizException(ex.getMessage());
        } finally {
            try {
                FileUtil.delete(zipFile);
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * apply generate
     *
     * @param codeGenerate the {@link DevCodeGenerate} instance
     * @return list of {@link CodeContent}
     */
    public List<CodeContent> doGenerate(DevCodeGenerate codeGenerate) throws BizException {
        Long templateCategoryId = codeGenerate.getTemplateCategoryId();
        List<DevCodeGenerateTemplate> templates =
                codeGeneratorTemplateService.list(Wrappers.lambdaQuery(DevCodeGenerateTemplate.class).eq(DevCodeGenerateTemplate::getCategoryId, templateCategoryId));
        CodeGenerateSource source = codeGenerate.getSource();
        CodeGenerator codeGenerator = codeGeneratorManager.obtainCodeGenerator(source);
        return codeGenerator.generate(codeGenerate, templates);
    }
}