package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.OptionalDouble;

public interface SharedSourcePathContext<T extends GraphNode> {
    GraphPath<T> findPath(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination);
    OptionalDouble pathCost(RoutableGraph<T, ? extends RouteLink<T>> graph, T destination);
 }
