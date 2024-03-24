package rfinder.query.result;

import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public class RideSegment extends PathSegment {

    private final RouteID routeID;
    private final int sourceSequence;

    private final List<Location> shape;

    public RideSegment(StopView sourceStop, StopView destinationStop, RouteID routeID, int sourceSequence, List<Location> shape) {
        super(sourceStop, destinationStop, shape);
        this.routeID = routeID;
        this.sourceSequence = sourceSequence;
        this.shape = shape;
    }

    @Override
    public StopView getSource() {
        return (StopView) super.getSource();
    }

    @Override
    public StopView getDestination() {
        return (StopView) super.getDestination();
    }

    public RouteID getRouteID() {
        return routeID;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }
}
