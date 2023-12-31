package rfinder.model.network.transport;

import rfinder.structures.common.Location;
import rfinder.structures.graph.Graph;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideLink;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public interface TransportGraph extends Graph<StopNode> {
    public abstract Set<StopNode> adjacentStops(Location loc);

    @Override
    Set<RideLink> getLinks(StopNode node);
    
    Set<RideLink> getLinks(StopNode node, boolean continued);
}
