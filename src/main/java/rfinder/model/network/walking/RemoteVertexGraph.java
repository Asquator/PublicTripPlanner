package rfinder.model.network.walking;

import rfinder.dao.DefaultDAO;
import rfinder.dao.RoadDAO;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.VertexNode;

import java.util.Set;

public class RemoteVertexGraph implements RoutableGraph<VertexNode> {

    private final RoadDAO dao = new DefaultDAO();

    @Override
    public Set<RouteLink<VertexNode>> getLinks(VertexNode node) {
        return dao.getRoadLinks(node);
    }
}
