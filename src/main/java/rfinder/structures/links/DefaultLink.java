package rfinder.structures.links;

public class DefaultLink <T> implements Link<T> {
    private final T target;
    public DefaultLink(T target) {
        this.target = target;
    }

    @Override
    public T target() {
        return target;
    }
}
