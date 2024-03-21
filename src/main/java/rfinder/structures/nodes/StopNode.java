package rfinder.structures.nodes;


import rfinder.structures.common.Location;
import rfinder.structures.graph.GraphNode;

import java.util.Objects;

public final class StopNode extends PathNode implements GraphNode<Integer> {

    public StopNode(Location location, int stopId){
        super(location, stopId);
    }

    public StopNode(Location location, String stopId){
        super(location, Integer.parseInt(stopId));
    }
}
