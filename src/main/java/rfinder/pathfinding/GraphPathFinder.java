package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;


public interface GraphPathFinder<T extends GraphNode, L extends RouteLink<T>> {
    GraphPath<T> findPath(T source, T destination);
}
