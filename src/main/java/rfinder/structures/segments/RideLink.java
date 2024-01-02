package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.graph.Link;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

public class RideLink extends NetworkTripLink{
    private TripPatternID tripPatternID;

    public RideLink(StopNode destinationNode, TripPatternID tripPatternID) {
        super(destinationNode);
        this.tripPatternID = tripPatternID;
    }
}
