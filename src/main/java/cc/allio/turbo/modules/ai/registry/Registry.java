package cc.allio.turbo.modules.ai.registry;

import java.util.Collection;
import java.util.Set;

/**
 * simple for registry
 *
 * @author j.x
 * @since 0.2.0
 */
public interface Registry<V, Key> {

    /**
     * get value by key
     *
     * @param key the key
     * @return the value instance or null
     */
    V get(Key key);

    /**
     * put value in registry
     *
     * @param key the key
     * @param v   the value
     */
    void put(Key key, V v);

    /**
     * remove element by key in registry
     *
     * @param key the key
     * @return true if remove success
     */
    boolean remove(Key key);

    /**
     * get all values.
     */
    Collection<V> getAll();

    /**
     * get all keys.
     */
    Set<Key> getKeys();

    /**
     * clear all elements in registry
     */
    void clear();
}
