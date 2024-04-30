package rfinder.cache;

import rfinder.cache.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FactoryCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cacheStorage = new ConcurrentHashMap<>();

    private final FactoryFunction<K,V> factory;


    public interface FactoryFunction<K,V> {
        V create(K key, Object... args);
    }

    public FactoryCache(FactoryFunction<K, V> factory) {
        this.factory = factory;
    }

    public V get(K key, Object... args) {
        return cacheStorage.computeIfAbsent(key, k -> factory.create(key, args));
    }

    @Override
    public V get(K key) {
        return cacheStorage.computeIfAbsent(key, k->factory.create(key));
    }

    @Override
    public void remove(K key) {
        cacheStorage.remove(key);
    }
}
