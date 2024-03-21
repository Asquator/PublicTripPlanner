package rfinder.structures.components;

import rfinder.structures.nodes.StopNode;

public class NetworkTripSegment extends PathSegment{

    public NetworkTripSegment(StopNode sourceNode, StopNode destinationNode) {
        super(sourceNode, destinationNode);
    }

    @Override
    public StopNode getSource() {
        return (StopNode) super.getSource();
    }

    public StopNode target() {
        return (StopNode) super.getDestination();
    }
}
