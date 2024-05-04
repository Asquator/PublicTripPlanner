package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.OptionalDouble;

public interface SourcePathContext<T extends GraphNode> {
    GraphPath<T> findPath(T destination);

    OptionalDouble pathCost(T destination);
 }
