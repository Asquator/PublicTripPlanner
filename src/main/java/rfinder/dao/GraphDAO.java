package rfinder.dao;

import rfinder.pathfinding.EdgeCut;
import rfinder.pathfinding.InMemoryNetworkGraph;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.*;

import java.util.HashMap;
import java.util.Set;

public interface GraphDAO {
    HashMap<PathNode, Set<ShapedLink>> getFullRoadGraph();
    EdgeCut getEdgeCut(PathNode source, PathNode target, Location loc1, Location loc2);

    InMemoryNetworkGraph getNetworkGraph();

    EdgeLinkage getEdgeLinkage(Location location, NodeFactory nodeFactory);

}
