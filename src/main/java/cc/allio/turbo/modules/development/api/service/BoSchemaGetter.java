package cc.allio.turbo.modules.development.api.service;

import cc.allio.turbo.common.getter.Getter;
import cc.allio.turbo.modules.development.domain.BoSchema;

/**
 * marked {@link BoSchema} getter interface
 *
 * @author j.x
 * @date 2024/3/6 20:23
 * @since 0.1.1
 */
public interface BoSchemaGetter extends Getter {

    /**
     * get {@link BoSchema}
     *
     * @return BoSchema
     */
    default BoSchema getBoSchema() {
        return null;
    }
}
