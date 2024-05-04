package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RouteLink;

import java.util.List;

public interface TotalSourcePathFinder<T extends GraphNode, L extends RouteLink<T>> extends GraphPathFinder<T, L> {
    List<PathRecord<? extends T>> getAllComputed(T source); // <node, score>
}
