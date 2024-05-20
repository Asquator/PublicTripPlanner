package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.OptionalDouble;

public class CachedPathFinder <T extends GraphNode<Integer>, L extends RouteLink<T>> implements GraphPathFinder<T, L> {

    protected final FactoryCache<T, ? extends SourcePathContext<T>> cache;
    protected final RoutableGraph<T, L> graph;

    public CachedPathFinder(RoutableGraph<T, L> graph, FactoryCache<T, ? extends SourcePathContext<T>> cache) {
        this.graph = graph;
        this.cache = cache;
    }

    @Override
    public GraphPath<T> findPath(T source, T destination) {
        return getContext(source).findPath(destination);
    }

    @Override
    public OptionalDouble pathCost(T source, T destination) {
        return getContext(source).pathCost(destination);
    }

    @Override
    public GraphPath<T> findPath(T source, T destination, RoutableGraph<T, L> graph) {
        return null;
    }

    @Override
    public OptionalDouble pathCost(T source, T destination, RoutableGraph<T, L> graph) {
        return OptionalDouble.empty();
    }


    public SourcePathContext<T> getContext(T source) {
        SourcePathContext<T> context = cache.get(source, graph);

        if(context == null) {
            throw new RuntimeException("Couldn't acquire for source: " + source);
        }

        return context;
    }
}
