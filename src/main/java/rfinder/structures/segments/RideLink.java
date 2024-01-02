package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.graph.Link;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.Objects;

public class RideLink extends NetworkTripLink {

    private final TripPatternID tripPatternID;
    private final int sourceSequence;
    private final int destinationSequence;
    private double distance;

    public RideLink(StopNode destinationNode, TripPatternID tripPatternID, int sourceSequence, int destinationSequence, double distance) {
        super(destinationNode);
        this.tripPatternID = tripPatternID;
        this.sourceSequence = sourceSequence;
        this.destinationSequence = destinationSequence;
        this.distance = distance;
    }

    public TripPatternID getTripPatternID() {
        return tripPatternID;
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
                Objects.equals(tripPatternID, rideLink.tripPatternID);
    }

    @Override
    public String toString() {
        return super.toString() + tripPatternID;
    }
}
