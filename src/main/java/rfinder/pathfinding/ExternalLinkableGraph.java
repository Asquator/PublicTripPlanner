package rfinder.pathfinding;

import rfinder.structures.common.UnorderedPair;
import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.links.EdgeData;

public interface ExternalLinkableGraph<T extends GraphNode<?>, L extends RouteLink<T>> extends RoutableGraph<T, L> {
    EdgeData<T> getEdgeData(UnorderedPair<T> edgeId);
}

