package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import lombok.Data;

/**
 * module model
 *
 * @author j.x
 * @date 2024/5/3 17:10
 * @since 0.1.1
 */
@Data
public class Module {

    /**
     * module name
     */
    private String name;

    /**
     * module key
     */
    private String key;

    /**
     * module packagePath
     */
    private String packagePath;

    /**
     * module request path
     */
    private String requestPath;

    /**
     * module belong to system
     */
    private String system;

    /**
     * module version
     */
    private String version;

    /**
     * author
     */
    private String author;

    /**
     * from {@link DevCodeGenerate} create new instance of {@link Module}
     *
     * @return the {@link Module} instance
     */
    public static Module from(DevCodeGenerate codeGenerate) {
        Module module = new Module();
        module.setName(codeGenerate.getModuleName());
        module.setKey(codeGenerate.getModuleKey());
        module.setPackagePath(codeGenerate.getModulePackagePath());
        module.setRequestPath(codeGenerate.getModuleRequestPath());
        module.setVersion(codeGenerate.getModuleVersion());
        module.setSystem(codeGenerate.getSystem());
        module.setAuthor(codeGenerate.getModuleAuthor());
        return module;
    }
}
