package rfinder.structures.nodes;

import rfinder.pathfinding.graph.GraphNode;
import rfinder.structures.general.Location;

public class VertexNode extends PathNode implements GraphNode<Integer> {

    private int vertexId;

    public VertexNode(Location location, int vertexId){
        super(location);
        this.vertexId = vertexId ;
    }

    public Integer getId() {
        return vertexId;
    }

    @Override
    public String toString() {
        return super.toString() + "[vertex id: " + vertexId + "]";
    }
}
