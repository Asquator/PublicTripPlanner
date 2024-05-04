package rfinder.pathfinding;

import rfinder.structures.common.UnorderedPair;
import rfinder.structures.links.EdgeData;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.nodes.PathNode;

import java.util.Map;
import java.util.Set;

public class InMemoryNetworkGraph implements ExternalLinkableGraph<PathNode, ShapedLink>{

    private final Map<PathNode, Set<ShapedLink>> connections; // <node, links>
    private final Map<UnorderedPair<PathNode>, EdgeData<PathNode>> edgeData;


    public InMemoryNetworkGraph(Map<PathNode, Set<ShapedLink>> connections, Map<UnorderedPair<PathNode>, EdgeData<PathNode>> edgeData) {
        this.connections = connections;
        this.edgeData = edgeData;
    }

    @Override
    public Set<ShapedLink> getLinks(PathNode node) {
        return connections.get(node);
    }

    @Override
    public EdgeData<PathNode> getEdgeData(UnorderedPair<PathNode> edgeId) {
        return edgeData.getOrDefault(edgeId, new EdgeData<>());
    }
}
