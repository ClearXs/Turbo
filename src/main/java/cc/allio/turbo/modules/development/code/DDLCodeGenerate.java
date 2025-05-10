package cc.allio.turbo.modules.development.code;

import cc.allio.turbo.modules.development.entity.DevCodeGenerate;
import cc.allio.turbo.modules.development.entity.DevCodeGenerateTemplate;
import cc.allio.turbo.modules.development.enums.CodeGenerateSource;
import cc.allio.turbo.modules.development.vo.CodeContent;

import java.util.List;

public class DDLCodeGenerate implements CodeGenerator {

    @Override
    public List<CodeContent> generate(DevCodeGenerate codeGenerate, List<DevCodeGenerateTemplate> templates) {



        return List.of();
    }

    @Override
    public CodeGenerateSource getSource() {
        return CodeGenerateSource.DDL;
    }
}
