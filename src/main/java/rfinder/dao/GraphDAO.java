package rfinder.dao;

import rfinder.pathfinding.EdgeLinkage;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.*;

import java.util.HashMap;
import java.util.Set;

public interface GraphDAO {



    HashMap<PathNode, Set<ShapedLink>> getFullRoadGraph();

    HashMap<PathNode, Set<ShapedLink>> getFullNetworkGraph();

    EdgeLinkage getLinkage(Location location, NodeFactory nodeFactory);



}
