package cc.allio.turbo.modules.developer.code;

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
    public String name;

    /**
     * module key
     */
    public String key;

    /**
     * module packagePath
     */
    public String packagePath;

    /**
     * module request path
     */
    public String requestPath;

    /**
     * module belong to system
     */
    public String system;

    /**
     * module version
     */
    public String version;

    /**
     * author
     */
    public String author;
}
