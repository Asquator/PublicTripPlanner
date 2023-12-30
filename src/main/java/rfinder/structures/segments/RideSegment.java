package rfinder.structures.segments;

import rfinder.structures.common.TripPatternID;
import rfinder.structures.graph.Link;
import rfinder.structures.nodes.StopNode;

import java.util.Objects;
import java.util.OptionalInt;

public class RideSegment extends NetworkTripSegment implements Link<StopNode> {

    private TripPatternID tripPatternID;

    private OptionalInt sourceSequence;
    private OptionalInt destinationSequence;

    public RideSegment(StopNode source, StopNode destination, TripPatternID tripPatternID){
        super(source, destination);
        this.tripPatternID = tripPatternID;
    }

    public OptionalInt getSourceSequence() {
        return sourceSequence;
    }

    public OptionalInt getDestinationSequence() {
        return destinationSequence;
    }

    public void setSourceSequence(int newSourceSequence) {
        sourceSequence = OptionalInt.of(newSourceSequence);
    }

    public void setDestinationSequence(int newDestinationSequence) {
        this.destinationSequence = OptionalInt.of(newDestinationSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripPatternID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideSegment that = (RideSegment) o;
        boolean res = Objects.equals(tripPatternID, that.tripPatternID);

        // if sequence numbers are specified, test their equality
        if(sourceSequence.isPresent() && destinationSequence.isPresent())
            res = res && Objects.equals(sourceSequence, that.sourceSequence)
                && Objects.equals(destinationSequence, that.destinationSequence);

        return res;
    }

    public TripPatternID getTripPatternID() {
        return tripPatternID;
    }

    @Override
    public String toString() {
        return super.toString() + "[trip pattern " + tripPatternID + "]";
    }

    @Override
    public double getWeight() {
        return getDistance();
    }
}
