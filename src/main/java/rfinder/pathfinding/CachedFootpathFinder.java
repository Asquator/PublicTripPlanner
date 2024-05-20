package rfinder.pathfinding;

import rfinder.structures.links.ShapedLink;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;

public class CachedFootpathFinder extends TotalCachedSourcePathFinder<PathNode, ShapedLink> {

    public CachedFootpathFinder(RoutableGraph<PathNode, ShapedLink> graph, FootpathCache cache){
        super(graph, cache);
    }
}


