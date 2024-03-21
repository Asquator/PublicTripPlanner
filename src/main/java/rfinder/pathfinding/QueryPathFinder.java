package rfinder.pathfinding;

import rfinder.dao.DefaultFootpathDAO;
import rfinder.dao.FootpathDAO;
import rfinder.query.NodeLinkageResolver;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.model.network.walking.ExtendedQueryGraph;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.OptionalDouble;

public class QueryPathFinder {

    private final GraphPathFinder<PathNode> graphPathFinder;

    private final FootpathDAO footpathDAO = new DefaultFootpathDAO();

    private final ExtendedQueryGraph extendedGraph;

    private NodeLinkageResolver resolver = new NodeLinkageResolver();
    private final QueryGraphInfo graphInfo;

    public QueryPathFinder(RoutableGraph<PathNode> originalGraph, QueryInfo queryInfo){
        graphInfo = new QueryGraphInfo(queryInfo, new NodeLinkageResolver());
        extendedGraph = new ExtendedQueryGraph(originalGraph, graphInfo);
        graphPathFinder = new CachedAsPathFinder<>(extendedGraph,  new HaversineDistanceEvaluator(), queryInfo.walkRadius());
    }

    public Path<PathNode> findPath(PathNode source, PathNode destination){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(source, destination);
        return new Path<>(graphPath.getPath(), graphPath.getLength());
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
