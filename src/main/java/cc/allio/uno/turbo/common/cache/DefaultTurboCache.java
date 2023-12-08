package cc.allio.uno.turbo.common.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存默认实现
 *
 * @author j.x
 * @date 2023/12/1 09:57
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public class DefaultTurboCache<T> extends PrefixKeyRedisCache<T> {

    private final String name;

}
