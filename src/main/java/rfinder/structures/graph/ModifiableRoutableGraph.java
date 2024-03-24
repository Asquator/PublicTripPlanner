package rfinder.structures.graph;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;

public interface ModifiableRoutableGraph<T extends GraphNode, L extends RouteLink<T>> extends RoutableGraph<T, L> {
    void addVertex(T node);

    void addEdge(T v1, T v2, double distance);
}
