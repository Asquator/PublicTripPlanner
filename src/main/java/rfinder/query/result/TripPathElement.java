package rfinder.query.result;

import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;

import java.time.Duration;
import java.util.List;

public final class TripPathElement extends SegmentPathElement {
    private final RouteID routeID;
    private final int sourceSequence;
    private final int destSequence;


    public TripPathElement(List<Location> shape, Duration duration, double km, RouteID routeID, int sourceSequence, int destSequence){
        super(shape, duration, km);
        this.routeID = routeID;
        this.sourceSequence = sourceSequence;
        this.destSequence = destSequence;
    }

    public int numOfStops(){
        return destSequence - sourceSequence;
    }

    public RouteID getRouteID() {
        return routeID;
    }
}
