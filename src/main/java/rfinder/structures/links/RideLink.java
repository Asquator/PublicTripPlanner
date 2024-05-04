package rfinder.structures.links;

import rfinder.structures.common.TripInstance;
import rfinder.dynamic.label.Multilabel;
import rfinder.query.result.NetworkQueryContext;
import rfinder.query.result.PathSegment;
import rfinder.query.result.RideSegment;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public non-sealed class RideLink extends LabeledLink {
    private final RouteID routeID;
    private int tripSequence;
    private int sourceSequence;
    private int destSequence;

    public RideLink(StopNode sourceNode, int sourceSequence, RouteID routeID, int tripSequence, Multilabel label) {
        super(sourceNode, label);
        this.routeID = routeID;
        this.tripSequence = tripSequence;
        this.sourceSequence = sourceSequence;
    }

    public void setDestSequence(int destSequence) {
        this.destSequence = destSequence;
    }

    public int getTripSequence() {
        return tripSequence;
    }

    public RouteID getRouteID() {
        return routeID;
    }

    @Override
    public RideLink clone() throws CloneNotSupportedException {
        RideLink clone = (RideLink) super.clone();
        clone.sourceSequence = sourceSequence;
        clone.destSequence = destSequence;
        clone.tripSequence = tripSequence;
        return clone;
    }

    @Override
    public PathSegment toSegment(PathNode next, Multilabel nextLabel, NetworkQueryContext queryContext) {
        List<Location> shape = queryContext.routeDAO().getShapeAlongRoute(routeID, target().getLocation(), next.getLocation());
        RideSegment rideSegment = new RideSegment(routeID, sourceSequence, destSequence, shape);

        TripInstance tripInstance = queryContext.queryContext().tripRepository().getRelevantTrips(routeID).get(tripSequence);
        rideSegment.setStartTimeStamp(tripInstance.stopTimes().get(sourceSequence));
        rideSegment.setEndTimeStamp(tripInstance.stopTimes().get(destSequence));

        return rideSegment;
    }

    @Override
    public String toString() {
        return super.toString() +"RideLink{" +
                "tripSequence=" + tripSequence +
                '}';
    }



}
