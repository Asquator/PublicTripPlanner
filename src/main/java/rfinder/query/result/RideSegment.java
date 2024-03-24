package rfinder.query.result;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

public class NetworkTripSegment extends PathSegment {

    private final RouteID routeID;
    private final int sourceSequence;
    private final int destSequence;


    public NetworkTripSegment(StopNode sourceNode, StopNode destinationNode, RouteID routeID, int sourceSequence, int destSequence) {
        super(sourceNode, destinationNode);
        this.routeID = routeID;
        this.sourceSequence = sourceSequence;
        this.destSequence = destSequence;
    }

    @Override
    public StopNode getSource() {
        return (StopNode) super.getSource();
    }

    public StopNode target() {
        return (StopNode) super.getDestination();
    }

    public RouteID getRouteID() {
        return routeID;
    }

    public int numOfStops(){
        return destSequence - sourceSequence;
    }
}
