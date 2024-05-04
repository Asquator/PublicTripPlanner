package rfinder.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FactoryCache<K, V> {
    protected class CacheElement{
        public long lastAccessed = System.currentTimeMillis();
        public V value;

        public CacheElement(V value){
            this.value = value;
        }
    }

    private final Map<K, CacheElement> cacheStorage = new ConcurrentHashMap<>();

    private final FactoryFunction<K,V> factory;


    public interface FactoryFunction<K,V> {
        V create(K key, Object... args);
    }

    public FactoryCache(FactoryFunction<K, V> factory, long timeToLiveMillis, long timeBetweenScansMillis){
        this.factory = factory;

        ScheduledExecutorService cleanerExecutor = Executors.newSingleThreadScheduledExecutor();
        cleanerExecutor.scheduleAtFixedRate(() -> {
            for(Map.Entry<K, CacheElement> entry : cacheStorage.entrySet()){
                if(System.currentTimeMillis() - entry.getValue().lastAccessed > timeToLiveMillis){
                    remove(entry.getKey());
                }
            }

        }, timeBetweenScansMillis, timeBetweenScansMillis, java.util.concurrent.TimeUnit.MILLISECONDS);

    }

    public V get(K key, Object... args) {
        CacheElement cacheElement = cacheStorage.computeIfAbsent(key, k -> new CacheElement(factory.create(key, args)));
        cacheElement.lastAccessed = System.currentTimeMillis();
        return cacheElement.value;
    }


    public void remove(K key) {
        cacheStorage.remove(key);
    }
}
