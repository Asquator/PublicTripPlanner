package rfinder.pathfinding;

import rfinder.dao.DefaultFootpathDAO;
import rfinder.dao.DefaultTransferDAO;
import rfinder.dao.FootpathDAO;
import rfinder.dao.TransferDAO;
import rfinder.pathfinding.heuristic.EuclideanDistanceEvaluator;
import rfinder.query.QueryGraphInfo;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.links.ShapedPath;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public class QueryFootpathFinder implements  FootpathManager {

    private final TotalSourcePathFinder<PathNode, ShapedLink> graphPathFinder;
    private final FootpathDAO footpathDAO = new DefaultFootpathDAO();
    private final ExtendedQueryGraph directGraph;

    public QueryFootpathFinder(ExtendedQueryGraph queryGraph, TotalSourcePathFinder<PathNode, ShapedLink> graphPathFinder){
        this.graphPathFinder = graphPathFinder;

        QueryGraphInfo queryGraphInfo = queryGraph.getQueryGraphInfo();
        directGraph = new ExtendedQueryGraph(queryGraph, queryGraphInfo);

        queryGraph.getLinks(queryGraphInfo.destinationRepr()).forEach(link ->
        {
            directGraph.addLink(link.target(), link.reversed(queryGraphInfo.destinationRepr()));
        });
    }

    /**
     * Finds the shortest path between two nodes
     * @param source source node
     * @param destination destination node
     * @return shortest shaped path
     */
    public ShapedPath findPath(PathNode source, PathNode destination){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(source, destination);

        // if failed to find a path, look in another direction
        if(graphPath == null){
            graphPath = graphPathFinder.findPath(destination, source);

            if(graphPath == null)
                return null;

            return new ShapedPath(graphPath.path().reversed(), graphPath.length(), directGraph.extractShape(graphPath)).reversed();
        }


        return new ShapedPath(graphPath.path(), graphPath.length(), directGraph.extractShape(graphPath));
    }

    public OptionalDouble pathCost(PathNode source, PathNode destination) {
        OptionalDouble pathCost = graphPathFinder.pathCost(source, destination);

        if (pathCost.isEmpty()){
            pathCost = graphPathFinder.pathCost(destination, source);
        }

        return pathCost;
    }

    public OptionalDouble directPathCost(){
        PrecomputedAsContext<PathNode> context = (PrecomputedAsContext<PathNode>) graphPathFinder.getContext(directGraph.getQueryGraphInfo().sourceRepr());
        return context.pathCost(directGraph.getQueryGraphInfo().destinationRepr(), directGraph);
    }


    @Override
    public List<PathRecord<? extends PathNode>> getFootpaths(PathNode source) {
        List<PathRecord<? extends PathNode>> allComputed = graphPathFinder.getAllComputed(source);

        if(allComputed != null)
            return allComputed;

        Set<StopNode> footpaths = source.getFootpathsWith(footpathDAO, directGraph.getQueryGraphInfo().walkRadius());
        return graphPathFinder.tryComputeToAll(source, footpaths, directGraph);
    }

}
