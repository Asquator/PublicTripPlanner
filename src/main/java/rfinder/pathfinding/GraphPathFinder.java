package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.Optional;
import java.util.OptionalDouble;


public interface GraphPathFinder<T extends GraphNode> {
    GraphPath<T> findPath(T source, T destination);
    OptionalDouble pathCost(T source, T destination);
}
