package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

public class NetworkTripLink extends PathLink{
    public NetworkTripLink(StopNode destinationNode) {
        super(destinationNode);
    }

    @Override
    public PathNode getDestination() {
        return (StopNode)super.getDestination();
    }
}
