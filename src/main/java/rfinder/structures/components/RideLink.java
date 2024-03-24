package rfinder.structures.components;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

public class TripSequentialLink extends NetworkTripLink{

    private final int tripSequence;
    private final RouteID routeID;
    private final int sourceSequence;
    private final int destSequence;

    public TripSequentialLink(StopNode sourceNode, RouteID routeID, int tripSequence, int sourceSequence, int destSequence) {
        super(sourceNode);
        this.routeID = routeID;
        this.tripSequence = tripSequence;
        this.sourceSequence = sourceSequence;
        this.destSequence = destSequence;
    }

    public int getTripSequence() {
        return tripSequence;
    }

    public RouteID getRouteID() {
        return routeID;
    }

    public int getDestSequence() {
        return destSequence;
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
