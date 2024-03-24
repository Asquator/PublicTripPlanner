package rfinder.dao;

import rfinder.pathfinding.EdgeLinkage;
import rfinder.pathfinding.ShapedLink;
import rfinder.structures.common.Location;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.*;

import java.util.HashMap;
import java.util.Set;

public interface RoadDAO {

    /**
     * Retrieves locaiton of a vertex
     * @param vertexId unique vertex ID
     * @return location of a vertex
     */
    Location getVertexLocation(int vertexId);



    VertexNode getVertexByID(int id);

    Set<RouteLink<VertexNode>> getRoadLinks(VertexNode vertexNode);

    HashMap<PathNode, Set<ShapedLink>> getFullRoadGraph();

    HashMap<PathNode, Set<ShapedLink>> getFullNetworkGraph();

    EdgeLinkage getLinkage(Location location, NodeFactory nodeFactory);

    EdgeLinkage getLinkage(Location location, int knownId);


}
