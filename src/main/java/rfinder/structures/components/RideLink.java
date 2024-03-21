package rfinder.structures.components;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.util.Objects;

public class RideLink extends NetworkTripLink {

    private final RouteID routeID;
    private final int sourceSequence;
    private final int destinationSequence;
    private double distance;

    public RideLink(StopNode destinationNode, RouteID routeID, int sourceSequence, int destinationSequence, double distance) {
        super(destinationNode);
        this.routeID = routeID;
        this.sourceSequence = sourceSequence;
        this.destinationSequence = destinationSequence;
        this.distance = distance;
    }

    public RouteID getTripPatternID() {
        return routeID;
    }

    public int getSourceSequence() {
        return sourceSequence;
    }


    public double getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideLink rideLink = (RideLink) o;
        return sourceSequence == rideLink.sourceSequence &&
                destinationSequence == rideLink.destinationSequence &&
                Objects.equals(routeID, rideLink.routeID);
    }

    @Override
    public String toString() {
        return super.toString() + routeID;
    }
}
