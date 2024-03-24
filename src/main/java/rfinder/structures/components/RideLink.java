package rfinder.structures.components;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

public class RideLink extends NetworkTripLink{
    private final RouteID routeID;
    private final int tripSequence;
    private final int sourceSequence;

    public RideLink(StopNode sourceNode, RouteID routeID, int tripSequence, int sourceSequence) {
        super(sourceNode);
        this.routeID = routeID;
        this.tripSequence = tripSequence;
        this.sourceSequence = sourceSequence;
    }

    public int getTripSequence() {
        return tripSequence;
    }

    public RouteID getRouteID() {
        return routeID;
    }


    public int getSourceSequence() {
        return sourceSequence;
    }

    @Override
    public String toString() {
        return super.toString() +"TripSequentialLink{" +
                "tripSequence=" + tripSequence +
                '}';
    }
}
