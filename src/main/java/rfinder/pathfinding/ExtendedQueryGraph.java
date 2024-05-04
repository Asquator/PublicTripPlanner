package rfinder.pathfinding;

import rfinder.dao.GraphDAO;
import rfinder.query.QueryGraphInfo;
import rfinder.structures.common.Location;
import rfinder.structures.common.UnorderedPair;
import rfinder.structures.links.EdgeData;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.nodes.PathNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ExtendedQueryGraph extends ExtendedRoutableGraph<PathNode, ShapedLink> {

    public ExtendedQueryGraph(ExternalLinkableGraph<PathNode, ShapedLink> originalGraph, QueryGraphInfo queryGraphInfo) {
        super(originalGraph);

        final EdgeLinkage sourceLinkage = queryGraphInfo.sourceLinkage();
        final EdgeLinkage destLinkage = queryGraphInfo.destinationLinkage();

        GraphDAO dao = queryGraphInfo.graphDAO();

        // retrieve vertex adapters to extend the original graph
        final PathNode sourceRepr = sourceLinkage.closest();
        final PathNode destRepr = destLinkage.closest();

        // retrieve the two edges
        EdgeData<PathNode> sourceEdge = originalGraph.getEdgeData(new UnorderedPair<>(sourceLinkage.source(), sourceLinkage.target()));
        EdgeData<PathNode> destEdge = originalGraph.getEdgeData(new UnorderedPair<>(destLinkage.source(), destLinkage.target()));

        // ensure that edge points are linked
        sourceEdge.addLinkage(sourceLinkage.source());
        sourceEdge.addLinkage(sourceLinkage.target());

        destEdge.addLinkage(destLinkage.source());
        destEdge.addLinkage(destLinkage.target());

        // link source to every node linked to this edge
        sourceEdge.linkIterator().forEachRemaining(node -> {
            EdgeCut edgeCut = dao.getEdgeCut(sourceLinkage.source(), sourceLinkage.target(), sourceRepr.getLocation(), node.getLocation());
            addLink(sourceRepr, new ShapedLink(node, edgeCut.km(), edgeCut.shape()));
        });

        // link destination to every node linked to this edge
        destEdge.linkIterator().forEachRemaining(node -> {
            EdgeCut edgeCut = dao.getEdgeCut(destLinkage.source(), destLinkage.target(), destRepr.getLocation(), node.getLocation());
            addLink(destRepr, new ShapedLink(node, edgeCut.km(), edgeCut.shape()));
        });

    }

    /**
     * Extract the shape of the given path
     * @param graphPath path in this graph
     * @return geometric shape of the path
     */
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

    // return the shape between two nodes
    private List<Location> getShape(PathNode source, PathNode destination){
        return getLinks(source).stream()
                .filter(link -> link.target() == destination)
                .map(ShapedLink::getShape)
                .findFirst()
                .orElse(null);
    }

}
