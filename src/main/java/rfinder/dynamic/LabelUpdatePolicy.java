package rfinder.dynamic;

import rfinder.structures.components.PathLink;

public interface LabelUpdatePolicy<T> {
    void update(Multilabel transferLabel, T newValue, PathLink backwardLink);
}
