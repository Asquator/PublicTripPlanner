package rfinder.model.network.walking;

import rfinder.dao.RoadDAO;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;

import java.util.HashMap;
import java.util.Set;

public class InMemoryNetworkGraph implements RoutableGraph<PathNode> {

    private final HashMap<PathNode, Set<RouteLink<PathNode>>> connections; // <node, links>

    protected RoadDAO dao;

    public InMemoryNetworkGraph(RoadDAO dao){
        this.dao = dao;
        connections = dao.getFullNetworkGraph();
        System.out.println("Initialized graph, total nodes " + connections.size());
    }

    @Override
    public Set<RouteLink<PathNode>> getLinks(PathNode node) {
        return connections.get(node);
    }
}
