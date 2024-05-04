package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.List;

public class TotalCachedSourcePathFinder<T extends GraphNode<Integer>, L extends RouteLink<T>> extends CachedPathFinder<T, L> implements TotalSourcePathFinder<T, L> {
    public TotalCachedSourcePathFinder(RoutableGraph<T, L> graph, FactoryCache<T, TotalSourcePathContext<T>> cache) {
        super(graph, cache);
    }

    @Override
    public List<PathRecord<? extends T>> getAllComputed(T source) {
        return ((TotalSourcePathContext<T>) cache.get(source, graph)).getAllComputed();
    }
}
