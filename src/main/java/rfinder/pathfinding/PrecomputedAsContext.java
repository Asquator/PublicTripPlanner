package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.*;

public class PrecomputedAsContext<T extends GraphNode> extends AsSourceContext<T> implements TotalSourcePathContext<T> {

    private List<PathRecord<? extends T>> computedList = new ArrayList<>();

    public PrecomputedAsContext(RoutableGraph<T, ? extends RouteLink<T>> graph, T source, HeuristicEvaluator<T> heuristicEvaluator) {
        super(graph, source, heuristicEvaluator);
    }

    public PrecomputedAsContext(RoutableGraph<T, ? extends RouteLink<T>> graph, T source, HeuristicEvaluator<T> heuristicEvaluator, List<PathRecord<? extends T>> computedList) {
        super(graph,source, heuristicEvaluator);
        this.computedList = computedList;
    }


    public synchronized List<PathRecord<? extends T>> getAllComputed() {
        return computedList;
    }

    public synchronized void tryComputeToAll(Collection<? extends T> nodes) {
        for (T node : nodes){
            OptionalDouble cost = pathCost(node);
            if(cost.isPresent())
                computedList.add(new PathRecord<>(node, cost.getAsDouble()));
        }
    }

}
