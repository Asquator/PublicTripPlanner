package rfinder.model.network.transport;

import rfinder.dao.PostgisRouteDAO;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class PostgisTransportGraph extends TransportGraph {
    private final PostgisRouteDAO dao = new PostgisRouteDAO();

    @Override
    public Set<RideSegment> getLinks(StopNode node) {
        return dao.getTransportLinks(node);
    }

    @Override
    public Set<StopNode> adjacentStops(Location location) {
        return null;
    }
}

