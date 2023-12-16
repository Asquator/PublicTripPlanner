package rfinder.structures.nodes;

import rfinder.structures.general.Location;

public class VertexNode extends PathNode {

    private int vertexId;

    public VertexNode(Location location, int vertexId){
        super(location);
        this.vertexId = vertexId ;
    }

    public int getVertexId() {
        return vertexId;
    }

    @Override
    public String toString() {
        return super.toString() + "[vertex id: " + vertexId + "]";
    }
}
