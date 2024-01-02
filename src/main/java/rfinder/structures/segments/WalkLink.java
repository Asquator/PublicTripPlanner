package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.nodes.StopNode;

public class WalkLink extends NetworkTripLink{
    public WalkLink(StopNode destinationNode) {
        super(destinationNode);
    }
}
