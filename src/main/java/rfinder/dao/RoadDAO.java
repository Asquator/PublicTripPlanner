package rfinder.dao;

import rfinder.model.network.walking.EdgeLinkage;
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

    /**
     * Retrieves closest routable vertex by location
     * @param location location
     * @return closest routable vertex to location
     */
    VertexNode getClosestVertex(Location location);

    /**
     * Retrieves closest vertex to a stop, convenience method, equivalent to getClosestVertex(stopNode.getLocation())
     * @param stopNode stop node
     * @return closest routable vertex to stop
     */
    VertexNode getClosestVertex(StopNode stopNode);


    VertexNode getVertexByID(int id);

    Set<RouteLink<VertexNode>> getRoadLinks(VertexNode vertexNode);

    HashMap<PathNode, Set<RouteLink<PathNode>>> getFullRoadGraph();

    HashMap<PathNode, Set<RouteLink<PathNode>>> getFullNetworkGraph();

    EdgeLinkage getLinkage(Location location, NodeFactory nodeFactory);

    EdgeLinkage getLinkage(Location location, int knownId);

}
