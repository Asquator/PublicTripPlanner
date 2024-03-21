package rfinder.structures.components;

import rfinder.structures.nodes.StopNode;

public class NetworkTripLink extends PathLink {
    public NetworkTripLink(StopNode destinationNode) {
        super(destinationNode);
    }

    @Override
    public StopNode target() {
        return (StopNode)super.target();
    }
}
