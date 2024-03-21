/*
package rfinder.structures.components;

import rfinder.structures.common.RouteID;
import rfinder.structures.graph.WeightedGraphLink;
import rfinder.structures.nodes.StopNode;

import java.util.Objects;
import java.util.OptionalInt;

public class RideSegment extends NetworkTripSegment implements WeightedGraphLink<StopNode> {

    private RouteID routeID;

    private OptionalInt sourceSequence;
    private OptionalInt destinationSequence;

    public RideSegment(StopNode source, StopNode destination, RouteID routeID){
        super(source, destination);
        this.routeID = routeID;
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
        return Objects.hash(routeID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideSegment that = (RideSegment) o;
        boolean res = Objects.equals(routeID, that.routeID);

        // if sequence numbers are specified, test their equality
        if(sourceSequence.isPresent() && destinationSequence.isPresent())
            res = res && Objects.equals(sourceSequence, that.sourceSequence)
                && Objects.equals(destinationSequence, that.destinationSequence);

        return res;
    }

    public RouteID getTripPatternID() {
        return routeID;
    }

    @Override
    public String toString() {
        return super.toString() + "[trip pattern " + routeID + "]";
    }

    @Override
    public double weight() {
        return getDistance();
    }
}
*/
