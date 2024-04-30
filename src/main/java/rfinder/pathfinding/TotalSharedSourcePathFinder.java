package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.List;

public interface TotalSharedSourcePathFinder<T extends GraphNode, L extends RouteLink<T>> extends GraphPathFinder<T, L> {
    List<PathRecord<T>> getAllComputed(RoutableGraph<T, ? extends RouteLink<T>> graph, T source); // <node, score>
}
