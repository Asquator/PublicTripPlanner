package rfinder.model.network.walking;

import rfinder.query.QueryGraphInfo;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;

public class ExtendedQueryGraph extends ExtendedGraph<PathNode> {

    public ExtendedQueryGraph(RoutableGraph<PathNode> originalGraph, QueryGraphInfo queryGraphInfo) {
        super(originalGraph);

        final EdgeLinkage sourceLinkage = queryGraphInfo.sourceLinkage();
        final EdgeLinkage destLinkage = queryGraphInfo.destinationLinkage();

        final PathNode sourceRepr = sourceLinkage.closest();
        final PathNode destRepr = destLinkage.closest();

        // create vertex adapters to extend the original graph
        // add links to connection sets

        //connect source temporary to two adjacent nodes
        addLink(sourceRepr, new RouteLink<>(sourceLinkage.source(), sourceLinkage.kmSource()));
        addLink(sourceRepr, new RouteLink<>(sourceLinkage.target(), sourceLinkage.kmTarget()));

        //connect two adjacent nodes destination temporary
        addLink(destLinkage.source(), new RouteLink<>(destRepr, destLinkage.kmSource()));
        addLink(destLinkage.target(), new RouteLink<>(destRepr, destLinkage.kmTarget()));
    }
}
