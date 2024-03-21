package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;

import java.util.*;

public class CachedAsPathFinder<T extends GraphNode<Integer>> implements GraphPathFinder<T> {

    private final Map<T, SourcePathContext<T>> contextCache = new HashMap<>();
    private final HeuristicEvaluator<T> heuristicEvaluator;
    private final RoutableGraph<T> graph;

    private final double maxPruningScore;

    public CachedAsPathFinder(RoutableGraph<T> graph, HeuristicEvaluator<T> heuristicEvaluator, double defaultMaxPruningScore){
        this.graph = graph;
        this.heuristicEvaluator = heuristicEvaluator;
        this.maxPruningScore = defaultMaxPruningScore;
    }

    @Override
    public GraphPath<T> findPath(T source, T destination) {
        return getContext(source).findPath(destination);
    }

    @Override
    public OptionalDouble pathCost(T source, T destination) {
        return getContext(source).pathCost(destination);
    }

    private SourcePathContext<T> getContext(T source) {
        SourcePathContext<T> context;

        if (contextCache.containsKey(source)) {
            context = contextCache.get(source);
        } else {
            context = new AsSourceContext<T>(graph, source, heuristicEvaluator);
            ((AsSourceContext<T>) context).setMaxScore(maxPruningScore);
            contextCache.put(source, context);
        }

        return context;
    }
}
