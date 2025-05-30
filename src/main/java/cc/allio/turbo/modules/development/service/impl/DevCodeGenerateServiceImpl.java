package cc.allio.turbo.modules.development.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.util.WebUtil;
import cc.allio.turbo.modules.development.code.CodeGenerator;
import cc.allio.turbo.modules.development.code.CodeGeneratorManager;
import cc.allio.turbo.modules.development.enums.CodeGenerateSource;
import cc.allio.turbo.modules.development.enums.CodeTemplateDomain;
import cc.allio.turbo.modules.development.vo.CodeContent;
import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.development.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.development.service.IDevCodeGeneratorTemplateService;
import cc.allio.uno.core.util.concurrent.LockContext;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletResponse;
import jodd.io.ZipBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        List<CodeContent> codeContents = doGenerate(codeGenerate);
        InputStream zipIO = withZip(codeContents);
        WebUtil.writeToOutputStream(codeGenerate.getInstanceName() + ".zip", zipIO, response);
    }

    @Override
    public void generateFile(Long id, String filename, HttpServletResponse response) throws BizException {
        DevCodeGenerate codeGenerate = details(id);
        if (codeGenerate == null) {
            throw new BizException("Not found code generate entity by id");
        }
        List<CodeContent> codeContents = doGenerate(codeGenerate);

        CodeContent specificCodeFile =
                codeContents.stream()
                        .filter(codeContent -> codeContent.getFilename().equals(filename))
                        .findFirst()
                        .orElse(null);

        if (specificCodeFile != null) {
            InputStream io = withSpecificContext(specificCodeFile);
            WebUtil.writeToOutputStream(filename, io, response);
        }
        throw new BizException(String.format("Not found %s file", filename));
    }

    @Override
    public void batchGenerate(List<Long> id, HttpServletResponse response) {
        List<DevCodeGenerate> devCodeGenerates = listByIds(id);
        // loop for method of doGenerate and get list of CodeContent
        List<CodeContent> codeContents =
                devCodeGenerates.stream()
                        .map(this::doGenerate)
                        .flatMap(Collection::stream)
                        .toList();
        InputStream zipIO = withZip(codeContents);
        WebUtil.writeToOutputStream("generation" + ".zip", zipIO, response);
    }

    /**
     * apply generate
     *
     * @param codeGenerate the {@link DevCodeGenerate} instance
     * @return list of {@link CodeContent}
     */
    List<CodeContent> doGenerate(DevCodeGenerate codeGenerate) {
        Long templateCategoryId = codeGenerate.getTemplateCategoryId();
        List<DevCodeGenerateTemplate> templates =
                codeGeneratorTemplateService.list(Wrappers.lambdaQuery(DevCodeGenerateTemplate.class).eq(DevCodeGenerateTemplate::getCategoryId, templateCategoryId));
        CodeGenerateSource source = codeGenerate.getSource();
        CodeGenerator codeGenerator = codeGeneratorManager.obtainCodeGenerator(source);
        return codeGenerator.generate(codeGenerate, templates);
    }

    /**
     * zip all list of {@link CodeContent}
     *
     * @param codeContents the list of {@link CodeContent}
     * @return return zip file {@link InputStream}
     */
    InputStream withZip(List<CodeContent> codeContents) {
        return LockContext.lock()
                .lockReturn(() -> {
                    ZipBuilder zipBuilder = ZipBuilder.createZipInMemory();
                    for (CodeContent codeContent : codeContents) {
                        // file content
                        String content = codeContent.getContent();
                        // like as /backend/pages/controller/TestController.java
                        String domain = Optional.ofNullable(codeContent.getCodeDomain()).orElse(CodeTemplateDomain.OTHER).getValue();
                        String dir = codeContent.getFilepath();
                        String filepath = domain + dir + StringPool.SLASH + codeContent.getFilename();
                        // save the code content
                        zipBuilder = zipBuilder.add(content).path(filepath).save();
                    }
                    return new ByteArrayInputStream(zipBuilder.toBytes());
                })
                .unchecked();
    }

    /**
     * get specific {@link CodeContent} {@link InputStream}
     *
     * @param codeContent the {@link CodeContent} instance
     * @return return {@link InputStream}
     */
    InputStream withSpecificContext(CodeContent codeContent) {
        // file content
        String content = codeContent.getContent();
        return new ByteArrayInputStream(content.getBytes());
    }
}
