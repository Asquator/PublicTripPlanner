package rfinder.cache;

public interface Cache <K,V>{

    V get(K key);

    void remove(K key);
}
