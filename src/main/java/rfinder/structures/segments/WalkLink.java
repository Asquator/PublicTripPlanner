package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

public class WalkLink extends PathLink{
    public WalkLink(PathNode destinationNode) {
        super(destinationNode);
    }
}
