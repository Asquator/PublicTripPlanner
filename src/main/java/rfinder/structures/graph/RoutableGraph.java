package rfinder.structures.graph;

import rfinder.pathfinding.graph.RoutingNode;

import java.util.Set;

public interface RoutableGraph <T extends GraphNode> extends Graph<T>{
    @Override
    Set<RouteLink<T>> getLinks(T node);
}
