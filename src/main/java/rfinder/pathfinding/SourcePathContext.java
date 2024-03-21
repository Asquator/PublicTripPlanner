package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.Optional;
import java.util.OptionalDouble;

public interface SourcePathContext<T extends GraphNode> {
    GraphPath<T> findPath(T destination);
    OptionalDouble pathCost(T destination);
}
