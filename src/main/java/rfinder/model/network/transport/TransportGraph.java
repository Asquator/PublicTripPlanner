package rfinder.model.network.transport;

import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.graph.Graph;
import rfinder.structures.nodes.StopNode;

import java.util.Iterator;
import java.util.Set;

public interface TransportGraph extends Graph<StopNode> {
    Set<StopNode> adjacentStops(Location loc);
    Iterator<StopNode> getStops(RouteID tripPattern);
}
