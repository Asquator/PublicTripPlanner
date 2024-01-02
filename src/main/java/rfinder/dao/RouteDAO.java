package rfinder.dao;

import rfinder.structures.common.Location;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;
import rfinder.structures.segments.RideLink;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public interface RouteDAO {

    /**
     * Retrieves location of a stop
     * @param stopId stop unique ID
     * @return location of a stop
     */
    Location getStopLocation(String stopId);

    Set<RideLink> getTransportLinks(StopNode stopNode, boolean continued);

    Set<StopNode> getStopsInRadius(Location location, double radius);
}
