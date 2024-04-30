package rfinder.dynamic.label;

public interface LabelUpdatePolicy<T> {
    void update(Multilabel transferLabel, T newValue);
}
