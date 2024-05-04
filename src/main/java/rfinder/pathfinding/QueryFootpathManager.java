package rfinder.pathfinding;

import rfinder.structures.links.ShapedLink;
import rfinder.structures.links.ShapedPath;
import rfinder.structures.nodes.PathNode;

import java.util.List;

public class QueryFootpathManager implements GraphPathFinder<PathNode, ShapedLink>, FootpathManager {

    private final TotalSourcePathFinder<PathNode, ShapedLink> graphPathFinder;
    private final ExtendedQueryGraph queryGraph;

    public QueryFootpathManager(ExtendedQueryGraph queryGraph, TotalSourcePathFinder<PathNode, ShapedLink> graphPathFinder){
        this.queryGraph = queryGraph;
        this.graphPathFinder = graphPathFinder;

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

            return new ShapedPath(graphPath.path().reversed(), graphPath.length(), queryGraph.extractShape(graphPath)).reversed();
        }


        return new ShapedPath(graphPath.path(), graphPath.length(), queryGraph.extractShape(graphPath));
    }

    @Override
    public List<PathRecord<? extends PathNode>> getFootpaths(PathNode source) {
        return graphPathFinder.getAllComputed(source);
    }

}
