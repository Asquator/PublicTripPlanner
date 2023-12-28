package rfinder.model.network.transport;

import rfinder.db.PostgisRouteDAO;
import rfinder.structures.graph.Graph;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class PostgisTransportGraph implements Graph<StopNode> {
    private final PostgisRouteDAO dao = new PostgisRouteDAO();

    @Override
    public Set<RideSegment> getLinks(StopNode node) {
        return dao.getTransportLinks(node);
    }
}
