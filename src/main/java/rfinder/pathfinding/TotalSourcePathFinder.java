package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.Collection;
import java.util.List;

public interface TotalSourcePathFinder<T extends GraphNode, L extends RouteLink<T>> extends GraphPathFinder<T, L> {

    List<PathRecord<? extends T>> getAllComputed(T source); // <node, score>
    List<PathRecord<? extends T>> tryComputeToAll(T source, Collection<? extends T> nodes, RoutableGraph<T, ? extends RouteLink<T>> graph);
    TotalSourcePathContext<T> getContext(T source);
}

