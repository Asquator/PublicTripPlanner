package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TotalSharedSourcePathContext<T extends GraphNode> extends SharedSourcePathContext<T> {

    void tryComputeToAll(RoutableGraph<T, ? extends RouteLink<T>> graph, Collection<? extends T> nodes);
    List<PathRecord<T>> getAllComputed(RoutableGraph<T, ? extends RouteLink<T>> graph); // <node, score>
}
