package rfinder.model.network.walking;

import rfinder.db.PostgisRouteDAO;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.VertexNode;

import java.util.HashMap;
import java.util.Set;

public class InMemoryPostgisVertexGraph implements RoutableGraph<VertexNode> {

    private HashMap<VertexNode, Set<RouteLink<VertexNode>>> connections;

    public InMemoryPostgisVertexGraph(PostgisRouteDAO dao){
        connections = dao.getFullGraph();
        System.out.println("Initialized graph, total nodes " + connections.size());
    }

    @Override
    public Set<RouteLink<VertexNode>> getLinks(VertexNode node) {
        return connections.get(node);
    }
}
