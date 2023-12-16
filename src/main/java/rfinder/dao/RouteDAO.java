package rfinder.dao;

import rfinder.structures.general.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;

import java.sql.SQLException;

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
    VertexNode getClosestVertex(Location location);
    VertexNode getClosestVertex(StopNode stopNode);


}
