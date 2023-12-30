package rfinder.model.network.walking;

import rfinder.dao.PostgisDAO;
import rfinder.dao.RoadDAO;
import rfinder.dao.RouteDAO;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.VertexNode;

import java.util.Set;

public class PostgisVertexGraph implements RoutableGraph<VertexNode> {

    private final RoadDAO dao = new PostgisDAO();

    @Override
    public Set<RouteLink<VertexNode>> getLinks(VertexNode node) {
        return dao.getRoadLinks(node);
    }
}
