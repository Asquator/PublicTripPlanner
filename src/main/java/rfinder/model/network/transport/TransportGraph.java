package rfinder.model.network.transport;

import rfinder.structures.common.Location;
import rfinder.structures.graph.Graph;
import rfinder.structures.nodes.StopNode;

import java.util.Set;

public abstract class TransportGraph implements Graph<StopNode> {
    public abstract Set<StopNode> adjacentStops(Location loc);
}
