package cc.allio.turbo.modules.ai;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * base on local {@link Map} registry
 *
 * @author j.x
 * @since 0.2.0
 */
public class LocalRegistry<V, Key> implements Registry<V, Key> {

    private final Map<Key, V> localCache = Maps.newConcurrentMap();

    @Override
    public V get(Key key) {
        return localCache.get(key);
    }

    @Override
    public void put(Key key, V v) {
        localCache.put(key, v);
    }

    @Override
    public boolean remove(Key key) {
        return localCache.remove(key) != null;
    }

    @Override
    public Collection<V> getAll() {
        return localCache.values();
    }

    @Override
    public Set<Key> getKeys() {
        return localCache.keySet();
    }

    @Override
    public void clear() {
        localCache.clear();
    }
}
