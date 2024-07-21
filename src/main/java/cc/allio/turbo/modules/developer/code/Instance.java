package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.api.Key;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import lombok.Data;

/**
 * module instance
 *
 * @author j.x
 * @date 2024/7/16 18:21
 * @since 0.1.1
 */
@Data
public class Instance {

    /**
     * instance name
     */
    private String name;

    /**
     * instance key
     */
    private Key key;

    /**
     * instance request path
     */
    private String requestPath;

    /**
     * instance version
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
    public static Instance from(DevCodeGenerate codeGenerate) {
        Instance instance = new Instance();
        instance.setName(codeGenerate.getInstanceName());
        instance.setKey(new Key(codeGenerate.getInstanceKey()));
        instance.setRequestPath(codeGenerate.getRequestPath());
        instance.setVersion(codeGenerate.getInstanceVersion());
        instance.setAuthor(codeGenerate.getAuthor());
        return instance;
    }
}
