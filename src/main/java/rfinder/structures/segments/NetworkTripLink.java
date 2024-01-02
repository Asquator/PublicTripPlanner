package rfinder.structures.segments;

import rfinder.structures.nodes.StopNode;

public class NetworkTripLink extends PathLink {
    public NetworkTripLink(StopNode destinationNode) {
        super(destinationNode);
    }

    @Override
    public StopNode getDestination() {
        return (StopNode)super.getDestination();
    }
}
