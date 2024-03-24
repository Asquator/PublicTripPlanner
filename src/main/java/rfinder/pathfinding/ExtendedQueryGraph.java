package rfinder.pathfinding;

import rfinder.query.QueryGraphInfo;
import rfinder.structures.common.Location;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;


import java.util.List;
import java.util.Set;

public class ExtendedQueryGraph extends ExtendedGraph<PathNode, ShapedLink> {

    public ExtendedQueryGraph(RoutableGraph<PathNode, ShapedLink> originalGraph, QueryGraphInfo queryGraphInfo) {
        super(originalGraph);

        final EdgeLinkage sourceLinkage = queryGraphInfo.sourceLinkage();
        final EdgeLinkage destLinkage = queryGraphInfo.destinationLinkage();

        // retrieve vertex adapters to extend the original graph
        final PathNode sourceRepr = sourceLinkage.closest();
        final PathNode destRepr = destLinkage.closest();

        List<Location> sourceShape = List.of(sourceRepr.getLocation(), sourceLinkage.closest().getLocation());
        List<Location> destShape = List.of(destLinkage.closest().getLocation(), destRepr.getLocation());

        // add links to connection sets

        //connect source temporary to two adjacent nodes
        addLink(sourceRepr, new ShapedLink(sourceLinkage.source(), sourceLinkage.kmSource(), sourceShape));
        addLink(sourceRepr, new ShapedLink(sourceLinkage.target(), sourceLinkage.kmTarget(), sourceShape));

        //connect two adjacent nodes to destination
        addLink(destLinkage.source(), new ShapedLink(destRepr, destLinkage.kmSource(), destShape));
        addLink(destLinkage.target(), new ShapedLink(destRepr, destLinkage.kmTarget(), destShape));
    }

    public List<Location> getShape(PathNode source, PathNode destination){
        Set<ShapedLink> links = getLinks(source);

        return getLinks(source).stream()
                .filter(link -> link.target() == destination)
                .map(ShapedLink::getShape)
                .findFirst()
                .orElse(null);
    }
}
