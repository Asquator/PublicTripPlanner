package rfinder.dao;

import rfinder.structures.common.Location;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public interface RouteDAO {

    /**
     * Retrieves location of a stop
     * @param stopId stop unique ID
     * @return location of a stop
     */
    Location getStopLocation(String stopId);


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

    double getEuclideanDistance(VertexNode node1, VertexNode node2);

    Set<RouteLink<VertexNode>> getRoadLinks(VertexNode vertexNode);

    Set<RideSegment> getTransportLinks(StopNode stopNode);

    Set<StopNode> getStopsInRadius(Location location, double radius);
}
