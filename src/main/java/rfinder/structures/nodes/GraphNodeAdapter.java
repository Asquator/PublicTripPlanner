package rfinder.structures.nodes;
import rfinder.structures.common.Location;
import rfinder.structures.graph.GraphNode;

public final class GraphNodeAdapter extends VertexNode implements GraphNode<Integer> {

    GraphNodeAdapter(int id, Location location) {
        super(location, id);
    }

}
