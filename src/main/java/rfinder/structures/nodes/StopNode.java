package rfinder.structures.nodes;

import rfinder.structures.common.Location;
import rfinder.structures.graph.Graph;
import rfinder.structures.graph.GraphNode;

import java.util.Objects;

public class StopNode extends PathNode implements GraphNode<String> {

     private final String stopId;

     public StopNode(Location location, String stopId){
         super(location);
         this.stopId = stopId;
     }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopNode stopNode = (StopNode) o;
        return Objects.equals(stopId, stopNode.stopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId);
    }

    public String getStopId() {
        return stopId;
    }

    @Override
    public String toString() {
         return "[stop id " + stopId + "]";
    }

    @Override
    public String getId() {
        return stopId;
    }
}
