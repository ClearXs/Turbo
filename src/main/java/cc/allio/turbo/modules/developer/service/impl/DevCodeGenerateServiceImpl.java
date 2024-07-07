package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudRepositoryServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.code.CodeGenerator;
import cc.allio.turbo.modules.developer.code.CodeGeneratorManager;
import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import cc.allio.turbo.modules.developer.constant.CodeTemplateDomain;
import cc.allio.turbo.modules.developer.vo.CodeContent;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.developer.service.IDevCodeGenerateService;
import cc.allio.turbo.modules.developer.service.IDevCodeGeneratorTemplateService;
import cc.allio.uno.core.concurrent.LockContext;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletResponse;
import jodd.io.FileUtil;
import jodd.io.IOUtil;
import jodd.io.ZipUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        LockContext.lock()
                .then(() -> {
                    // create temp directory
                    String tempDirPath = codeGenerate.getModuleName();
                    File tempFile = new File(tempDirPath);
                    if (!tempFile.exists()) {
                        boolean haveMake = tempFile.mkdir();
                        if (!haveMake) {
                            throw new BizException("Failed to create generate temp direction!");
                        }
                    }
                    // create zip file
                    List<CodeContent> codeContents = doGenerate(codeGenerate);
                    for (CodeContent codeContent : codeContents) {
                        // like as ./temp/backend/pages/controller/TestController.java
                        String domain = Optional.ofNullable(codeContent.getCodeDomain()).orElse(CodeTemplateDomain.OTHER).getValue();
                        String dir = codeContent.getFilepath();
                        String filepath = tempDirPath + StringPool.SLASH + domain + dir + StringPool.SLASH + codeContent.getFilename();
                        File codeFile = new File(filepath);

                        // create directory from file path input to temp
                        File codeDirectory = codeFile.getParentFile();
                        if (!codeDirectory.exists()) {
                            boolean newDirectory = codeDirectory.mkdirs();
                            if (!newDirectory) {
                                throw new BizException("Failed to create code directory!");
                            }
                        }
                        // create file
                        if (!codeFile.exists()) {
                            boolean newFile = codeFile.createNewFile();
                            if (!newFile) {
                                throw new BizException("Failed to create code file!");
                            }
                        }
                        FileUtil.writeBytes(codeFile, codeContent.getContent().getBytes());
                    }

                    File zipFile = ZipUtil.zip(tempFile);

                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipFile.getName(), StandardCharsets.UTF_8));
                    response.setContentType("application/force-download");
                    response.setCharacterEncoding("UTF-8");
                    var fileInputStream = new FileInputStream(zipFile);
                    IOUtil.copy(fileInputStream, response.getOutputStream());
                    FileUtil.delete(zipFile);
                    FileUtil.delete(tempFile);
                })
                .release()
                .unchecked();
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
