package cc.allio.turbo.modules.developer.code;

import cc.allio.turbo.common.api.Key;
import cc.allio.turbo.modules.system.entity.SysCategory;
import cc.allio.uno.core.StringPool;
import lombok.Data;

import java.util.Objects;

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
    private Key key;

    /**
     * from {@link SysCategory} create new instance of {@link Module}
     *
     * @return the {@link SysCategory} instance
     */
    public static Module from(SysCategory category) {
        Module module = new Module();
        module.setName(Objects.requireNonNullElse(category.getName(), StringPool.EMPTY));
        module.setKey(new Key(category.getCode()));
        return module;
    }
}
