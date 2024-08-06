package cc.allio.turbo.modules.office.documentserver.configurers.mapper;

import cc.allio.turbo.modules.office.documentserver.configurers.wrappers.DefaultFileWrapper;

/**
 * map to specifies {@link M} from {@link DefaultFileWrapper}
 *
 * @author j.x
 * @date 2024/5/9 22:37
 * @since 0.0.1
 */
public interface Mapper<W, M> {

    /**
     * map to {@link M}
     *
     * @param wrapper the {@link DefaultFileWrapper} instance
     * @return {@link M} instance
     */
    M toModel(W wrapper);
}
