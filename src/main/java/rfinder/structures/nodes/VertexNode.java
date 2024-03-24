package rfinder.structures.nodes;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.common.Location;

public non-sealed class VertexNode extends PathNode implements GraphNode<Integer> {

    public VertexNode(Location location, int vertexId) {
        super(location, vertexId);
    }

}
