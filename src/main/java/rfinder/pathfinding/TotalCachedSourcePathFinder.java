package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.Collection;
import java.util.List;

public class TotalCachedSourcePathFinder<T extends GraphNode<Integer>, L extends RouteLink<T>> extends CachedPathFinder<T, L> implements TotalSourcePathFinder<T, L> {
    public TotalCachedSourcePathFinder(RoutableGraph<T, L> graph, FactoryCache<T, TotalSourcePathContext<T>> cache) {
        super(graph, cache);
    }

    @Override
    public List<PathRecord<? extends T>> getAllComputed(T source) {
        return ((TotalSourcePathContext<T>) cache.get(source, graph)).getAllComputed();
    }

    @Override
    public List<PathRecord<? extends T>> tryComputeToAll(T source, Collection<? extends T> nodes, RoutableGraph<T, ? extends RouteLink<T>> graph) {
        return ((TotalSourcePathContext<T>) cache.get(source, graph)).tryComputeToAll(nodes);
    }

    @Override
    public TotalSourcePathContext<T> getContext(T source) {
        return (TotalSourcePathContext<T>) super.getContext(source);
    }
}
