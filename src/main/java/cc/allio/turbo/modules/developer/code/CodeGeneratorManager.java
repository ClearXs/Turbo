package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.modules.developer.constant.CodeGenerateSource;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * manage of {@link CodeGenerator}
 *
 * @author j.x
 * @date 2024/6/20 22:46
 * @since 0.1.1
 */
public class CodeGeneratorManager {

    private Map<CodeGenerateSource, CodeGenerator> generatorMap = Maps.newConcurrentMap();

    /**
     * registry {@link CodeGenerator} instance
     *
     * @param codeGenerator the {@link CodeGenerator} instance
     */
    public void registry(CodeGenerator codeGenerator) {
        if (codeGenerator != null) {
            generatorMap.put(codeGenerator.getSource(), codeGenerator);
        }
    }

    /**
     * obtain {@link CodeGenerator} by {@link CodeGenerateSource}
     *
     * @param source the {@link CodeGenerateSource}
     * @return the {@link CodeGenerator} instance
     */
    public CodeGenerator obtainCodeGenerator(CodeGenerateSource source) {
        return generatorMap.get(source);
    }
}
