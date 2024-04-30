package rfinder.query.result;

import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public class RideSegment extends PathSegment {

    private final RouteID routeID;
    private final int sourceSequence;

    private final int destSequence;

    private final List<Location> shape;

    public RideSegment(RouteID routeID, int sourceSequence, int destSequence, List<Location> shape) {
        super(shape);
        this.routeID = routeID;
        this.sourceSequence = sourceSequence;
        this.destSequence = destSequence;
        this.shape = shape;
    }


    public RouteID getRouteID() {
        return routeID;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }

    @Override
    public Color defaultColor() {
        return Color.BLUE;
    }

    @Override
    public String toString() {
        return routeID.routeId();
    }
}
