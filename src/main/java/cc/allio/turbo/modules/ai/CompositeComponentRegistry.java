package cc.allio.turbo.modules.ai;

import cc.allio.uno.core.function.lambda.ThrowingMethodFunction;
import cc.allio.uno.core.reflect.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * the spring component delegate and delegate other delegate.
 *
 * @author j.x
 * @since 0.2.0
 */
@Slf4j
public abstract class CompositeComponentRegistry<V, K> implements
        Registry<V, K>, InitializingBean, DisposableBean, ApplicationContextAware {

    private final boolean isAutoScan;
    protected final Class<K> keyType;
    protected final Class<V> valueType;
    private final Registry<V, K> delegate;
    private final ThrowingMethodFunction<V, K> getKeyFunc;

    protected ApplicationContext applicationContext;

    protected CompositeComponentRegistry(Registry<V, K> delegate,
                                         ThrowingMethodFunction<V, K> getKeyFunc) {
        this(true, delegate, getKeyFunc);
    }

    protected CompositeComponentRegistry(ThrowingMethodFunction<V, K> getKeyFunc) {
        this(true, new LocalRegistry<>(), getKeyFunc);
    }

    protected CompositeComponentRegistry(boolean isAutoScan,
                                         Registry<V, K> delegate,
                                         ThrowingMethodFunction<V, K> getKeyFunc) {
        Class<CompositeComponentRegistry> registryClass = CompositeComponentRegistry.class;
        this.isAutoScan = isAutoScan;
        this.valueType = (Class<V>) ReflectTools.getGenericType(this, registryClass, 0);
        this.keyType = (Class<K>) ReflectTools.getGenericType(this, registryClass, 1);
        this.getKeyFunc = getKeyFunc;
        this.delegate = delegate;
    }

    @Override
    public V get(K key) {
        return delegate.get(key);
    }

    @Override
    public void put(K key, V v) {
        delegate.put(key, v);
    }

    @Override
    public boolean remove(K key) {
        return delegate.remove(key);
    }

    @Override
    public Collection<V> getAll() {
        return delegate.getAll();
    }

    @Override
    public Set<K> getKeys() {
        return delegate.getKeys();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isAutoScan) {
            scan();
        }
        initialization();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        clear();
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    /**
     * scan existing spring container Value beans.
     */
    protected void scan() {
        if (applicationContext != null) {
            Map<String, V> valueTypeMap = applicationContext.getBeansOfType(valueType);
            valueTypeMap.values()
                    .forEach(v -> {
                        try {
                            K key = getKeyFunc.apply(v);
                            put(key, v);
                        } catch (Throwable ex) {
                            log.error("Get key value error", ex);
                        }
                    });
        }
    }

    protected void initialization() throws Exception {
    }
}
