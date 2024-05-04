package rfinder.dynamic.label;

public interface UpdatePrunePolicy {
    boolean updatePruneMergeRoute(Multilabel transferLabel, Object... params);

    void updateFootpaths(Multilabel transferLabel, Object... params);
}
