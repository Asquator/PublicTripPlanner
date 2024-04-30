package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.List;

public class TotalCachedPathFinder<T extends GraphNode<Integer>, L extends RouteLink<T>> extends CachedPathFinder<T, L> implements TotalSharedSourcePathFinder<T, L> {
    public TotalCachedPathFinder(RoutableGraph<T, L> graph, FactoryCache<T, TotalSharedSourcePathContext<T>> cache) {
        super(graph, cache);
    }


    @Override
    public List<PathRecord<T>> getAllComputed(RoutableGraph<T, ? extends RouteLink<T>> graph, T source) {
        return ((TotalSharedSourcePathContext<T>) cache.get(source, graph)).getAllComputed(graph);
    }
}
