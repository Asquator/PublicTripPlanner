package rfinder.structures.components;

import rfinder.structures.nodes.StopNode;

public class TripSequentialLink extends NetworkTripLink{

    private int tripSequence;
    public TripSequentialLink(StopNode destinationNode, int tripSequence) {
        super(destinationNode);
        this.tripSequence = tripSequence;
    }

    public int getTripSequence() {
        return tripSequence;
    }

    @Override
    public String toString() {
        return super.toString() +"TripSequentialLink{" +
                "tripSequence=" + tripSequence +
                '}';
    }
}
