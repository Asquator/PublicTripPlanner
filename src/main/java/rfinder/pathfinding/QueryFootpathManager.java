package rfinder.pathfinding;

import rfinder.query.QueryGraphInfo;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.links.ShapedPath;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public class QueryFootpathManager implements GraphPathFinder<PathNode, ShapedLink>, FootpathManager {

    private final TotalSharedSourcePathFinder<PathNode, ShapedLink> graphPathFinder;
    private final ExtendedQueryGraph queryGraph;

    public QueryFootpathManager(ExtendedQueryGraph queryGraph, TotalSharedSourcePathFinder<PathNode, ShapedLink> graphPathFinder, QueryGraphInfo graphInfo){
        this.queryGraph = queryGraph;
        this.graphPathFinder = graphPathFinder;

    }

    public ShapedPath findPath(PathNode source, PathNode destination){
        GraphPath<PathNode> graphPath = graphPathFinder.findPath(source, destination);
        if(graphPath == null){
            graphPath = graphPathFinder.findPath(destination, source);

            if(graphPath == null)
                return null;

            return new ShapedPath(graphPath.path().reversed(), graphPath.length(), queryGraph.extractShape(graphPath)).reversed();
        }


        return new ShapedPath(graphPath.path(), graphPath.length(), queryGraph.extractShape(graphPath));
    }

    @Override
    public List<PathRecord<PathNode>> getFootpaths(PathNode source) {
        return graphPathFinder.getAllComputed(queryGraph, source);
    }

    @Override
    public List<PathRecord<PathNode>> getFootpaths(StopNode source) {
        return graphPathFinder.getAllComputed(queryGraph, source);
    }


}
