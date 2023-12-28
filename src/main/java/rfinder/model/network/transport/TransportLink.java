package rfinder.model.network.transport;

import rfinder.structures.graph.Link;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Objects;

public final class TransportLink implements Link<StopNode> {
    private final RideSegment segment;

    public TransportLink(RideSegment segment) {
        this.segment = segment;
    }

    @Override
    public StopNode getDestination() {
        return segment.getDestination();
    }


    @Override
    public double getWeight() {
        return segment.getDistance();
    }

    public RideSegment segment() {
        return segment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TransportLink) obj;
        return Objects.equals(this.segment, that.segment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segment);
    }

    @Override
    public String toString() {
        return "TransportLink[" +
                "segment=" + segment + ']';
    }


}
