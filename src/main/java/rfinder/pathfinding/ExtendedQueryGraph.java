package rfinder.pathfinding;

import rfinder.query.QueryGraphInfo;
import rfinder.structures.common.Location;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ExtendedQueryGraph extends ExtendedRoutableGraph<PathNode, ShapedLink> {

    public ExtendedQueryGraph(RoutableGraph<PathNode, ShapedLink> originalGraph, QueryGraphInfo queryGraphInfo) {
        super(originalGraph);

        final EdgeLinkage sourceLinkage = queryGraphInfo.sourceLinkage();
        final EdgeLinkage destLinkage = queryGraphInfo.destinationLinkage();

        // retrieve vertex adapters to extend the original graph
        final PathNode sourceRepr = sourceLinkage.closest();
        final PathNode destRepr = destLinkage.closest();
        // add one-sided links to source and destination vertices

        //connect source temporary to two adjacent nodes
        addLink(sourceRepr, new ShapedLink(sourceLinkage.source(), sourceLinkage.kmSource(),
                List.of(sourceRepr.getLocation(), sourceLinkage.source().getLocation())));

        addLink(sourceRepr, new ShapedLink(sourceLinkage.target(), sourceLinkage.kmTarget(),
                List.of(sourceRepr.getLocation(), sourceLinkage.target().getLocation())));

        //connect destination temporary to two adjacent nodes
        addLink(destRepr, new ShapedLink(destLinkage.source(), destLinkage.kmSource(),
                List.of(destRepr.getLocation(), destLinkage.source().getLocation())));

        addLink(destRepr, new ShapedLink(destLinkage.target(), destLinkage.kmTarget(),
                List.of(destRepr.getLocation(), destLinkage.target().getLocation())));
    }


    List<Location> extractShape(GraphPath<PathNode> graphPath) {
        List<Location> shape = new ArrayList<>();
        PathNode current, next;
        ListIterator<? extends PathNode> nodeIterator = graphPath.path().listIterator();

        current = nodeIterator.next();

        while (nodeIterator.hasNext()) {
            next = nodeIterator.next();
            shape.addAll(getShape(current, next));
            current = next;
        }

        return shape;
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
