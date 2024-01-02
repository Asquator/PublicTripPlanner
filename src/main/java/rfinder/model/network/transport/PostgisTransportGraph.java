package rfinder.model.network.transport;

import rfinder.dao.PostgisDAO;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideLink;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class PostgisTransportGraph implements TransportGraph {
    private final PostgisDAO dao = new PostgisDAO();

    @Override
    public Set<RideLink> getLinks(StopNode node) {
        return dao.getTransportLinks(node, false);
    }

    @Override
    public Set<RideLink> getLinks(StopNode node, boolean continued) {
        return dao.getTransportLinks(node, continued);
    }

    @Override
    public Set<StopNode> adjacentStops(Location location) {
        return null;
    }

}

