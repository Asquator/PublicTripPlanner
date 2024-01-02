package rfinder.structures.segments;

import rfinder.structures.nodes.StopNode;

public class NetworkTripSegment extends PathSegment{

    public NetworkTripSegment(StopNode sourceNode, StopNode destinationNode) {
        super(sourceNode, destinationNode);
    }

    @Override
    public StopNode getSource() {
        return (StopNode) super.getSource();
    }

    @Override
    public StopNode getDestination() {
        return (StopNode) super.getDestination();
    }
}
