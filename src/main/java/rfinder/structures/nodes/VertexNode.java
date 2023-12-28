package rfinder.structures.nodes;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.common.Location;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexNode that = (VertexNode) o;
        return vertexId == that.vertexId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexId);
    }

    @Override
    public String toString() {
        return super.toString() + "[vertex id: " + vertexId + "]";
    }
}
