package rfinder.structures.nodes;

import rfinder.query.result.NominalPathElement;
import rfinder.query.result.PathElement;
import rfinder.structures.common.Location;
import rfinder.structures.graph.GraphNode;

import java.util.Objects;

public sealed class PathNode implements GraphNode<Integer> permits StopNode, VertexNode {
    private Location location;
    private final int id;

    public PathNode(Location location, int id){
        this.location = location;
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isStop(){
        return this instanceof StopNode;
    }

    public NominalPathElement toElement(){
        return new NominalPathElement(location);
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String toString() {
        return "PathNode{" +
                "location=" + location +
                ", id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathNode pathNode = (PathNode) o;
        return id == pathNode.id;
    }
}
