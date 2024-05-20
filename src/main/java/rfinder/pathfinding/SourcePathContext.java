package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.OptionalDouble;

public interface SourcePathContext<T extends GraphNode> {
    GraphPath<T> findPath(T destination);

    OptionalDouble pathCost(T destination);

    GraphPath<T> findPath(T destination, RoutableGraph<T, ? extends RouteLink<T>> graph);

    OptionalDouble pathCost(T destination, RoutableGraph<T, ? extends RouteLink<T>> graph);
 }
