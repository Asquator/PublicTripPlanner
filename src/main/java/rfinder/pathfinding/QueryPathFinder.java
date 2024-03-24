package rfinder.pathfinding;

import rfinder.query.NodeLinkageResolver;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.structures.common.Location;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.OptionalDouble;

public class QueryPathFinder {

    private final GraphPathFinder<PathNode, ShapedLink> graphPathFinder;
    private final ExtendedQueryGraph extendedGraph;

    private NodeLinkageResolver resolver = new NodeLinkageResolver();
    private final QueryGraphInfo graphInfo;

    public QueryPathFinder(RoutableGraph<PathNode, ShapedLink> originalGraph, QueryInfo queryInfo){
        graphInfo = new QueryGraphInfo(queryInfo, new NodeLinkageResolver());
        extendedGraph = new ExtendedQueryGraph(originalGraph, graphInfo);
        graphPathFinder = new CachedAsPathFinder<>(extendedGraph,  new HaversineDistanceEvaluator(), queryInfo.walkRadius());
    }

    public FootPath<PathNode> findPath(PathNode source, PathNode destination){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(source, destination);
        if(graphPath == null)
            return null;

        return new FootPath<>(graphPath.path(), graphPath.length(), extractShape(graphPath));
    }

    public OptionalDouble pathCost(PathNode source, PathNode destination){
        return graphPathFinder.pathCost(source, destination);
    }

    public PathNode getSourceRepr(){
        return graphInfo.sourceLinkage().closest();
    }

    public PathNode getDestinationRepr(){
        return graphInfo.destinationLinkage().closest();
    }

    public ExtendedQueryGraph getExtendedGraph() {
        return extendedGraph;
    }

    private List<Location> extractShape(GraphPath<PathNode> graphPath) {
        List<Location> shape = new ArrayList<>();
        PathNode current, next;
        ListIterator<? extends PathNode> nodeIterator = graphPath.path().listIterator();

        current = nodeIterator.next();

        while(nodeIterator.hasNext()){
            next = nodeIterator.next();
            shape.addAll(extendedGraph.getShape(current, next));
            current = next;
        }

        return shape;
    }

    /*

    public Path<PathNode> findFromSource(StopNode destination) {
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(extendedGraph.getSourceNode(), destination);
        return new Path<>(graphPath.getPath(), graphPath.getLength());
    }

    public OptionalDouble costFromSource(StopNode destination) {
        return graphPathFinder.pathCost(extendedGraph.getSourceNode(), destination);
    }

    public Path<PathNode> findToDestination(StopNode source){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(source, extendedGraph.getDestinationNode());
        return new Path<>(graphPath.getPath(), graphPath.getLength());
    }

    public OptionalDouble costToDestination(StopNode source){
        return graphPathFinder.pathCost(source, extendedGraph.getDestinationNode());
    }

    public Path<PathNode> directCastPath(){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(extendedGraph.getSourceNode(), extendedGraph.getDestinationNode());
        return new Path<>(graphPath.getPath(), graphPath.getLength());
    }

    public OptionalDouble directCastPathCost(){
        return graphPathFinder.pathCost(extendedGraph.getSourceNode(), extendedGraph.getDestinationNode());
    }
*/


}
